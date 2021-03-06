package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fontbonne.ley.clerc.lockbreaker.BuildConfig;
import com.fontbonne.ley.clerc.lockbreaker.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WearService extends WearableListenerService {

    // Tag for Logcat
    private static final String TAG = "WearService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // If no action defined, return
        if (intent.getAction() == null) return START_NOT_STICKY;

        // Match against the given action
        ACTION_SEND action = ACTION_SEND.valueOf(intent.getAction());
        PutDataMapRequest putDataMapRequest;
        switch (action) {
            case STARTACTIVITY:
                String activity = intent.getStringExtra(ACTIVITY_TO_START);
                sendMessage(activity, BuildConfig.W_path_start_activity);
                break;
            case MESSAGE:
                String message = intent.getStringExtra(MESSAGE);
                if (message == null) message = "";
                sendMessage(message, intent.getStringExtra(PATH));
                break;
            case EXAMPLE_DATAMAP:
                putDataMapRequest = PutDataMapRequest.create(BuildConfig.W_example_path_datamap);
                putDataMapRequest.getDataMap().putInt(BuildConfig.W_a_key, intent.getIntExtra(DATAMAP_INT, -1));
                putDataMapRequest.getDataMap().putIntegerArrayList(BuildConfig.W_some_other_key, intent.getIntegerArrayListExtra(DATAMAP_INT_ARRAYLIST));
                sendPutDataMapRequest(putDataMapRequest);
                break;
            case EXAMPLE_ASSET:
                putDataMapRequest = PutDataMapRequest.create(BuildConfig.W_example_path_asset);
                putDataMapRequest.getDataMap().putAsset(BuildConfig.W_some_other_key, (Asset) intent.getParcelableExtra(IMAGE));
                sendPutDataMapRequest(putDataMapRequest);
                break;
            case LUX:
                putDataMapRequest = PutDataMapRequest.create(intent.getStringExtra(PATH));
                putDataMapRequest.getDataMap().putFloat(BuildConfig.W_lux_value, intent.getFloatExtra(LUX, 1000));
                sendPutDataMapRequest(putDataMapRequest);
                break;
            case STEP:
                putDataMapRequest = PutDataMapRequest.create(BuildConfig.W_step_mobile);
                putDataMapRequest.getDataMap().putInt(BuildConfig.W_step_count, intent.getIntExtra(STEPCOUNT, 0));
                sendPutDataMapRequest(putDataMapRequest);
                break;
            case MAZE_CONTROLS:
                message = intent.getStringExtra(MESSAGE);
                if (message == null) message = "";
                sendMessage(message, BuildConfig.W_maze_controls_key);
                break;
            default:
                Log.w(TAG, "Unknown action");
                break;
        }

        return START_NOT_STICKY;
    }

    public static final String ACTIVITY_TO_START = "ACTIVITY_TO_START";

    public static final String MESSAGE = "MESSAGE";
    public static final String STEPCOUNT = "STEPCOUNT";
    public static final String DATAMAP_INT = "DATAMAP_INT";
    public static final String DATAMAP_INT_ARRAYLIST = "DATAMAP_INT_ARRAYLIST";
    public static final String IMAGE = "IMAGE";
    public static final String PATH = "PATH";
    public static final String LUX = "LUX";

    public static Asset createAssetFromBitmap(Bitmap bitmap) {
        bitmap = resizeImage(bitmap, 390);

        if (bitmap != null) {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static Bitmap resizeImage(Bitmap bitmap, int newSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Image smaller, return it as is!
        if (width <= newSize && height <= newSize) return bitmap;

        int newWidth;
        int newHeight;

        if (width > height) {
            newWidth = newSize;
            newHeight = (newSize * height) / width;
        } else if (width < height) {
            newHeight = newSize;
            newWidth = (newSize * width) / height;
        } else {
            newHeight = newSize;
            newWidth = newSize;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.v(TAG, "onDataChanged: " + dataEvents);

        for (DataEvent event : dataEvents) {

            // Get the URI of the event
            Uri uri = event.getDataItem().getUri();

            // Test if data has changed or has been removed
            if (event.getType() == DataEvent.TYPE_CHANGED) {

                // Extract the dataMap from the event
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());

                Log.v(TAG, "DataItem Changed: " + event.getDataItem().toString() + "\n"
                        + "\tPath: " + uri
                        + "\tDatamap: " + dataMapItem.getDataMap() + "\n");

                Intent intent;

                assert uri.getPath() != null;
                switch (uri.getPath()) {
                    case BuildConfig.W_example_path_asset:
                        // Extract the data behind the key you know contains data
                        Asset asset = dataMapItem.getDataMap().getAsset(BuildConfig.W_some_other_key);
                        intent = new Intent("REPLACE_THIS_WITH_A_STRING_OF_ACTION_PREFERABLY_DEFINED_AS_A_CONSTANT_IN_TARGET_ACTIVITY");
                        bitmapFromAsset(asset, intent, "REPLACE_THIS_WITH_A_STRING_OF_IMAGE_PREFERABLY_DEFINED_AS_A_CONSTANT_IN_TARGET_ACTIVITY");
                        break;
                    case BuildConfig.W_misleading_colors:
                        // Extract the data behind the key you know contains data
                        Log.e("TAG_PAT", "putting DATAMAP stuff");
                        ArrayList<Integer> arraylist = dataMapItem.getDataMap().getIntegerArrayList(BuildConfig.W_misleading_colors_array);
                        intent = new Intent(this, MisleadingColors.class);
                        intent.putExtra(MisleadingColors.MISLEADINGCOLORS_ARRAYLIST, arraylist);
                        startActivity(intent);
                        //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        break;
                    case BuildConfig.W_step_wear:
                        int stepCount = dataMapItem.getDataMap().getInt(BuildConfig.W_step_count, 0);
                        intent = new Intent(StepByStep.STEPINTENTWEAR);
                        intent.putExtra(StepByStep.STEPCOUNT, stepCount);
                        LocalBroadcastManager.getInstance(WearService.this).sendBroadcast(intent);
                        break;
                    default:
                        Log.v(TAG, "Data changed for unhandled path: " + uri);
                        break;
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.w(TAG, "DataItem deleted: " + event.getDataItem().toString());
            }

            // For demo, send a acknowledgement message back to the node that created the data item
            sendMessage("Received data OK!", BuildConfig.W_path_acknowledge, uri.getHost());
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        // A message has been received from the Wear API
        // Get the URI of the event
        String path = messageEvent.getPath();
        String data = new String(messageEvent.getData());
        Log.e("TAG_PAT", "Received a message for path " + path
                + " : \"" + data
                + "\", from node " + messageEvent.getSourceNodeId());

        if (path.equals(BuildConfig.W_path_start_activity)
                && data.equals(BuildConfig.W_mainactivity)) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainActivityIntent);
        }

        Log.d("NICO", String.valueOf(path.equals(BuildConfig.W_waldo)));



        switch (path) {
            case BuildConfig.W_path_start_activity:
                Log.v(TAG, "Message asked to open Activity");

                Intent startIntent = null;
                switch (data) {
                    case BuildConfig.W_mainactivity:
                        startIntent = new Intent(this, MainActivity.class);
                        break;
                    case BuildConfig.W_end_screen:
                        startIntent = new Intent(this, EndScreenActivity.class);
                        break;
                    case BuildConfig.W_tutorial:
                        startIntent = new Intent(this, WatchTutorialActivity.class);
                        break;
                }

                if (startIntent == null) {
                    Log.w(TAG, "Asked to start unhandled activity: " + data);
                    return;
                }
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startIntent);
                break;
            case BuildConfig.W_path_acknowledge:
                Log.v(TAG, "Received acknowledgment");
                break;
            case BuildConfig.W_misleading_colors:
                Log.e(TAG, "Message contained text. Return a datamap for demo purpose");
                ArrayList<Integer> arrayList = new ArrayList<>();
                Collections.addAll(arrayList, 5, 7, 9, 10);
                //Intent intent = new Intent(MainActivity.STARTACTIVITY);
                //LocalBroadcastManager.getInstance(WearService.this).sendBroadcast(intent);
                PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(BuildConfig.W_example_path_datamap);
                putDataMapRequest.getDataMap().putInt(BuildConfig.W_a_key, 42);
                putDataMapRequest.getDataMap().putIntegerArrayList(BuildConfig.W_some_other_key, arrayList);
                sendPutDataMapRequest(putDataMapRequest);
                break;
            case BuildConfig.W_space_word_answer:
                Intent intent_space_word = new Intent(this, SpaceWord.class);
                intent_space_word.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_space_word.putExtra(SpaceWord.ANSWER, data);
                startActivity(intent_space_word);
                break;
            case BuildConfig.W_perilous_journey:
                Intent intent_perilous_journey = new Intent(this, PerilousJourney.class);
                intent_perilous_journey.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_perilous_journey);
                break;
            case BuildConfig.W_step_by_step:
                Intent intent_step_by_step = new Intent(this, StepByStep.class);
                intent_step_by_step.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_step_by_step);
                break;
            case BuildConfig.W_symbols_key:
                Intent intent_symbols = new Intent(this, SymbolsActivity.class);
                intent_symbols.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_symbols.putExtra(SymbolsActivity.START_SYMBOLS, data);
                startActivity(intent_symbols);
                break;
            case BuildConfig.W_maze_controls_key:
                Intent intent_maze = new Intent(this, MazeControlsActivity.class);
                intent_maze.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_maze);
                break;
            case BuildConfig.W_invisible_maze_key:
                Intent intent_cells = new Intent(this, InvisibleMazeActivity.class);
                intent_cells.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_cells.putExtra(InvisibleMazeActivity.MAZE_CELLS, data);
                startActivity(intent_cells);
                break;
            case BuildConfig.W_waldo:
                Intent intent_waldo = new Intent(this, WaldoProfileActivity.class);
                intent_waldo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_waldo.putExtra(WaldoProfileActivity.WALDO_NAME, data);
                startActivity(intent_waldo);
                break;
            case BuildConfig.W_similar_answers:
                Log.d("NICOLAS", "HEY");
                Intent intent_similar = new Intent(this, SimilarAnswerActivity.class);
                intent_similar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_similar.putExtra(SimilarAnswerActivity.ANSWER, data);
                Log.d("NICOLAS", intent_similar.toString());

                startActivity(intent_similar);
                break;
            case BuildConfig.W_encrypted_key:
                Intent intent_encrypted = new Intent(this, EncryptedActivity.class);
                intent_encrypted.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_encrypted.putExtra(EncryptedActivity.ENCRYPTED_DATA, data);
                startActivity(intent_encrypted);
                break;
            default:
                Log.w(TAG, "Received a message for unknown path " + path + " : " + new String(messageEvent.getData()));
        }
    }

    private void sendMessage(String message, String path, final String nodeId) {
        // Sends a message through the Wear API
        Wearable.getMessageClient(this)
                .sendMessage(nodeId, path, message.getBytes())
                .addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Log.v(TAG, "Sent message to " + nodeId + ". Result = " + integer);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Message not sent. " + e.getMessage());
                    }
                });
    }

    private void sendMessage(String message, String path) {
        // Send message to ALL connected nodes
        sendMessageToNodes(message, path);
    }

    void sendMessageToNodes(final String message, final String path) {
        Log.v(TAG, "Sending message " + message);
        // Lists all the nodes (devices) connected to the Wear API
        Wearable.getNodeClient(this).getConnectedNodes().addOnCompleteListener(new OnCompleteListener<List<Node>>() {
            @Override
            public void onComplete(@NonNull Task<List<Node>> listTask) {
                List<Node> nodes = listTask.getResult();
                for (Node node : nodes) {
                    Log.v(TAG, "Try to send message to a specific node");
                    WearService.this.sendMessage(message, path, node.getId());
                }
            }
        });
    }

    void sendPutDataMapRequest(PutDataMapRequest putDataMapRequest) {
        putDataMapRequest.getDataMap().putLong("time", System.nanoTime());
        PutDataRequest request = putDataMapRequest.asPutDataRequest();
        request.setUrgent();
        Wearable.getDataClient(this)
                .putDataItem(request)
                .addOnSuccessListener(new OnSuccessListener<DataItem>() {
                    @Override
                    public void onSuccess(DataItem dataItem) {
                        Log.v(TAG, "Sent datamap.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Datamap not sent. " + e.getMessage());
                    }
                });
    }

    private void bitmapFromAsset(Asset asset, final Intent intent, final String extraName) {
        // Reads an asset from the Wear API and parse it as an image
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }

        // Convert asset and convert it back to an image
        Wearable.getDataClient(this).getFdForAsset(asset)
                .addOnCompleteListener(new OnCompleteListener<DataClient.GetFdForAssetResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataClient.GetFdForAssetResponse> runnable) {
                        Log.v(TAG, "Got bitmap from asset");
                        InputStream assetInputStream = runnable.getResult().getInputStream();
                        Bitmap bmp = BitmapFactory.decodeStream(assetInputStream);

                        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
                        byte[] bytes = byteStream.toByteArray();
                        intent.putExtra(extraName, bytes);
                        LocalBroadcastManager.getInstance(WearService.this).sendBroadcast(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception runnable) {
                        Log.e(TAG, "Failed to get bitmap from asset");
                    }
                });
    }

    // Constants
    public enum ACTION_SEND {
        STARTACTIVITY, MESSAGE, EXAMPLE_DATAMAP, EXAMPLE_ASSET, LUX, STEP, MAZE_CONTROLS
    }
}

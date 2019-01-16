package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceWordActivity extends MiniGame {

    public SpaceWordActivity(List<Class> gameActivity, int totscore, int difficulty, int gameStatus) {
        super(gameActivity, totscore, difficulty, gameStatus);
    }
    public SpaceWordActivity() {
        super();
    }

    private Button btnCheck;
    private EditText editText;
    private TextView nbrLettersText;

    private String answer;
    String[] letters;
    private String input;
    private int nbrGamesPlayed;
    private int gamesToPlay;
    private int amountScore;
    private static final Random RANDOM = new Random();
    private TextView time;

    MediaPlayer playercorrect;
    MediaPlayer playerwrong;

    // should change this to a database!
    private List<String> database = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_word);

        time = findViewById(R.id.timeView);
        editText = findViewById(R.id.editText);
        btnCheck = findViewById(R.id.button);
        nbrLettersText = findViewById(R.id.nbrLettersTextView);

        receiveLastGameData();

        playercorrect = MediaPlayer.create(this, R.raw.correctsfx);
        playerwrong = MediaPlayer.create(this, R.raw.wrongsfx);

        Log.d("DIFFICULTY", String.valueOf(difficulty));
        switch (difficulty){
            case 0:
                //easy DOESNT EXIST
                gamesToPlay = 1;
                amountScore = 300;
                break;
            case 1:
                // medium
                gamesToPlay = 2;
                amountScore = 150;
                break;
            case 2:
                // hard
                gamesToPlay = 4;
                amountScore = 100;
                break;
        }
        make_database();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = editText.getText().toString();
                if(input != null) {
                    if (answer.equals(input)) {
                        Log.e("TAG_RESULTS", "THIS IS CORRECT!!");
                        Toast.makeText(SpaceWordActivity.this,
                                "THIS IS CORRECT!!", Toast.LENGTH_SHORT).show();
                        playercorrect.start();
                        addtoScore(amountScore);
                    } else {
                        Log.e("TAG_RESULTS", "THIS IS WRONG! Answer is " + answer);
                        Toast.makeText(SpaceWordActivity.this,
                                "THIS IS WRONG!", Toast.LENGTH_SHORT).show();
                        playerwrong.start();
                    }
                    nbrGamesPlayed++;
                    if (nbrGamesPlayed < gamesToPlay) setupGame();
                    else{
                        initializeNextGame();
                        finish();
                    }
                }else {
                    Toast.makeText(SpaceWordActivity.this,
                            "Input a word to continue!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setupGame();
    }

    private void setupGame(){
        int index = RANDOM.nextInt(database.size());
        answer = database.get(index);
        database.remove(index);
        editText.setText("");
        nbrLettersText.setText("(" + answer.length() + "Letters)");
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.SPACEWORD.name());
        intent.putExtra(WearService.MESSAGE, answer);
        intent.putExtra(WearService.PATH, BuildConfig.W_space_word_answer);
        startService(intent);
    }

    @Override
    public void callbackTimer(){
        if(time != null){
            if(min_cur == 0) time.setTextColor(Color.RED);
            time.setText(Integer.toString(min_cur) + ":" + Integer.toString(sec_cur));
        }
    }
    @Override
    protected void onPause() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity

            Log.e("TAG_PAT", "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY");
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicGameService.class);
                stopService(intentmusic);
                Log.e("TAG_PAT", "YOU LEFT YOUR APP");
            }
            else {
                Log.e("TAG_PAT", "YOU SWITCHED ACTIVITIES WITHIN YOUR APP");
            }
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity

            Log.e("TAG_PAT", "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY");
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Log.e("TAG_PAT", "YOU LEFT YOUR APP");
            }
            else {
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicGameService.class);
                startService(intentmusic);
                Log.e("TAG_PAT", "YOU SWITCHED ACTIVITIES WITHIN YOUR APP");
            }
        }
        super.onResume();
    }

    private void make_database(){
        String json = loadJSONFromAsset(this);
        try {
            JSONObject reader = new JSONObject(json);
            int nbwords = reader.getInt("nb");

            for(int i = 0; i < nbwords; i++){
                String idx = ("w" + i);
                database.add(reader.getString(idx));
            }
        } catch (Exception e) {
            database.add("Error");
        }
    }

    private String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("spaceword.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}

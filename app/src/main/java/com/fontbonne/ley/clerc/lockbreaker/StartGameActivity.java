package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class StartGameActivity extends MiniGame {

    private Button mStartButton;
    private Button mOptionButton;
    private Button mStatButton;

    private static final int GET_DIFFICULTY = 1;


    public static int NBRMINIGAMES = 10;
    private int nbrGames = 10;


    public StartGameActivity(List<Class> gameActivity, int totscore, int difficulty, int gameStatus) {
        super(gameActivity, totscore, difficulty, gameStatus);
    }

    public StartGameActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String tag = intent.getStringExtra(FinalScreenActivity.RESTART_TAG);
        if(tag != null && tag.equals("RESTART")){
            setupGame();
            initializeNextGame();
            finish();
        }

        setContentView(R.layout.activity_start_game);
        Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicGameService.class);
        stopService(intentmusic);


        mStartButton = findViewById(R.id.playButton);
        mOptionButton = findViewById(R.id.optionButton);
        mStatButton = findViewById(R.id.statButton);



        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartButton.setEnabled(false);
                setupGame();
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicStartScreenService.class);
                stopService(intentmusic);
                intentmusic = new Intent(getApplicationContext(), BackgroundMusicGameService.class);
                stopService(intentmusic);
                startService(intentmusic);
                initializeNextGame();
            }
        });

        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionIntent = new Intent(StartGameActivity.this, OptionActivity.class);
                optionIntent.putExtra(OptionActivity.DIFFICULTY_TAG, difficulty);
                optionIntent.putExtra(OptionActivity.NUMBER_TAG, nbrGames);

                startActivityForResult(optionIntent, GET_DIFFICULTY);
            }
        });

        mStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statIntent = new Intent(StartGameActivity.this, StatActivity.class);
                startActivity(statIntent);
            }
        });

        sendStart();
    }


    private void setupGame() {
        // Add all minigames here to games
        games.clear();
        games.add(MazeControlsActivity.class);
        games.add(WaldoActivity.class);
        games.add(SimilarQuizActivity.class);
        games.add(MisleadingColorsActivity.class);
        games.add(SpaceWordActivity.class);
        games.add(PerilousJourneyActivity.class);
        games.add(StepByStepActivity.class);
        games.add(SymbolsActivity.class);
        games.add(InvisibleMazeActivity.class);
        games.add(EncryptedActivity.class);

        // Randomize the list of games
        Collections.shuffle(games);

        // make games be the size of nbrGames
        if (nbrGames < NBRMINIGAMES)
            for (int i = 0; i < (NBRMINIGAMES - nbrGames); i++)
                games.remove(games.size() - 1);

        // Add starting screen and final screen
        games.add(0, StartGameActivity.class);
        games.add(1, TutorialActivity.class);
        games.add(2,SpaceWordActivity.class);
        games.add(FinalScreenActivity.class);
    }

    public void sendStart() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STARTACTIVITY.name());
        intent.putExtra(WearService.ACTIVITY_TO_START, BuildConfig.W_mainactivity);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("DEBUGNICO","HEY2");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_DIFFICULTY && resultCode == RESULT_OK && data != null) {
            difficulty = (int) data.getIntExtra(OptionActivity.DIFFICULTY_TAG,1);
            nbrGames = (int) data.getIntExtra(OptionActivity.NUMBER_TAG,1);

            Log.d("DEBUGNICO1",String.valueOf(difficulty));
            Log.d("DEBUGNICO2",String.valueOf(nbrGames));


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
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicStartScreenService.class);
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
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicStartScreenService.class);
                startService(intentmusic);
                Log.e("TAG_PAT", "YOU SWITCHED ACTIVITIES WITHIN YOUR APP");
            }
        }
        mStartButton.setEnabled(true);
        //restart home screen when going back to home screen
        sendStart();
        super.onResume();
    }
}

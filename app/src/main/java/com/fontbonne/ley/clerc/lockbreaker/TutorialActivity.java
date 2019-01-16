package com.fontbonne.ley.clerc.lockbreaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;


public class TutorialActivity extends MiniGame {

    private static final int minutes = 0;
    private static final int seconds = 10;

    //constructors
    public TutorialActivity(List<Class> gameActivity, int totscore, int difficulty, int gameStatus) {
        super(gameActivity, totscore, difficulty, gameStatus);
    }

    public TutorialActivity() {
        super();
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        receiveLastGameData();

        Log.d("DIFFICULTY", String.valueOf(difficulty));
        switch (difficulty){
            case 0:
                //easy
                break;
            case 1:
                // medium
                break;
            case 2:
                // hard
                break;
        }
        
        startWatch();
    }

    private void startWatch() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STARTACTIVITY.name());
        intent.putExtra(WearService.ACTIVITY_TO_START, BuildConfig.W_tutorial);
        startService(intent);
    }


    public void startButtonCallback(View view) {
        Intent intent = new Intent(getApplicationContext(), BackgroundTimerService.class);
        intent.putExtra(MiniGame.MIN, minutes);
        intent.putExtra(MiniGame.SEC, seconds);
        startService(intent);
        initializeNextGame();
        finish();
    }
}

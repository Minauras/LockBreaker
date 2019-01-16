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

    //constructors
    public TutorialActivity(List<Class> gameActivity, int totscore, int difficulty) {
        super(gameActivity, totscore, difficulty);
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
        initializeNextGame();
        finish();
    }
}

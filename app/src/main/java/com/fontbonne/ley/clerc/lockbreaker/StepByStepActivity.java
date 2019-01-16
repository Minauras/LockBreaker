package com.fontbonne.ley.clerc.lockbreaker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class StepByStepActivity extends MiniGame {

    Button stepbtn;
    boolean my_turn = true;
    int step;
    int step_size = 100;
    ImageView godzillaImageView;
    ImageView japanImageView;

    float dpHeight;
    float dpWidth;

    AnimationDrawable godzillaWalking;
    AnimationDrawable godzillaCollapse;
    Handler timerHandler = new Handler();
    Runnable timerAnimation = new Runnable() {
        @Override
        public void run() {
            godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_walking);
            godzillaWalking = (AnimationDrawable) godzillaImageView.getBackground();
            godzillaWalking.stop();
            godzillaImageView.setX(dpWidth-(step)*step_size);
            score += 14;
            if(dpWidth-(step)*step_size <= 100){
                initializeNextGame();
                finish();
            }
        }
    };
    Runnable timerCollapse = new Runnable() {
        @Override
        public void run() {
            godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_collapse);
            godzillaCollapse = (AnimationDrawable) godzillaImageView.getBackground();
            godzillaCollapse.stop();
            godzillaImageView.setX(dpWidth-(step)*step_size);
            godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_walking);
            godzillaWalking = (AnimationDrawable) godzillaImageView.getBackground();
            score = -50;
        }
    };

    public static final String STEPCOUNT = "STEPCOUNT";
    public static final String STEPINTENTMOBILE = "STEPINTENTMOBILE";

    public StepByStepActivity(List<Class> gameActivity, int totscore, int difficulty, int gameStatus) {
        super(gameActivity, totscore, difficulty, gameStatus);
    }
    public StepByStepActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step);

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
        startWatchActivity();
        score = 0;

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        step_size = (int)(dpHeight/30);

        japanImageView = findViewById(R.id.japanImageView);
        japanImageView.setX(25);
        japanImageView.setY((int)(dpHeight/3));

        godzillaImageView = findViewById(R.id.animationImageView);
        godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_walking);
        godzillaWalking = (AnimationDrawable) godzillaImageView.getBackground();
        godzillaCollapse = (AnimationDrawable) godzillaImageView.getBackground();
        godzillaImageView.setX(dpWidth);
        godzillaImageView.setY((int)(dpHeight/3+50));

        stepbtn = findViewById(R.id.stepBtn);
        stepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(my_turn){
                    my_turn = false;
                    step += 1;
                    godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_walking);
                    godzillaWalking = (AnimationDrawable) godzillaImageView.getBackground();
                    godzillaWalking.start();
                    timerHandler.postDelayed(timerAnimation, 400);
                }else{
                    step = 1;
                    godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_collapse);
                    godzillaCollapse = (AnimationDrawable) godzillaImageView.getBackground();
                    godzillaCollapse.start();
                    timerHandler.postDelayed(timerCollapse, 400);
                }
                Log.e("TAG_PAT", "Mobile btn pressed Step Count: " + String.valueOf(step) + "; " + String.valueOf(score) + "; " + String.valueOf(totalScore));
                updateStepCount();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                my_turn = true;
                int temp = intent.getIntExtra(STEPCOUNT,0);
                if(temp > step) {
                    step = temp;
                    godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_walking);
                    godzillaWalking = (AnimationDrawable) godzillaImageView.getBackground();
                    godzillaWalking.start();
                    timerHandler.postDelayed(timerAnimation, 400);
                }else{
                    step = 1;
                    godzillaImageView.setBackgroundResource(R.drawable.animation_godzilla_collapse);
                    godzillaCollapse = (AnimationDrawable) godzillaImageView.getBackground();
                    godzillaCollapse.start();
                    timerHandler.postDelayed(timerCollapse, 400);
                }
                Log.e("TAG_PAT", "Mobile msg received Step Count: " + String.valueOf(step) + "; " + String.valueOf(score));
            }
        }, new IntentFilter(STEPINTENTMOBILE));
        timerHandler.postDelayed(timerAnimation, 0);
    }

    private void startWatchActivity() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STEPBYSTEP.name());
        startService(intent);
    }

    private void updateStepCount() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STEP.name());
        intent.putExtra(WearService.STEPCOUNT, step);
        startService(intent);
    }
}

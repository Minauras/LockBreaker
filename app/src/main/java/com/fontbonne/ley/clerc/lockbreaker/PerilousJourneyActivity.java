package com.fontbonne.ley.clerc.lockbreaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class PerilousJourneyActivity extends MiniGame {

    ImageView playerView;
    ImageView dragonView;
    AnimationDrawable dragonAnimation;
    AnimationDrawable runningAnimation;
    AnimationDrawable boxAnimation;
    TextView maxText;
    TextView progresstxt;
    TextView warningtxt;
    TextView scoreText;

    ProgressBar distanceRun;

    public static final String LUXVALUE = "LUXVALUE";
    public static final String SENDINGLUXVALUE = "SENDINGLUXVALUE";

    Random RANDOM = new Random();
    boolean dragon_coming;
    boolean is_running;
    boolean dragon_fly_by;
    int dragon_fly_by_count;
    float lux_value;
    boolean first_time;
    int Distance_to_run = 200;
    int progress_running = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    Runnable timerWarning = new Runnable() {
        @Override
        public void run() {
            dragon_fly_by = true;
            warningtxt.setText("");
            dragon_fly_by_count = 0;
            dragonAnimation.start();
            dragon_coming = false;
        }
    };

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if(is_running){
                progress_running += 1;
            }
            distanceRun.setProgress(progress_running);
            progresstxt.setText(String.valueOf(progress_running));
            if(dragon_fly_by){
                dragon_fly_by_count += 1;
                if(is_running) score -= 10;
            }
            scoreText.setText(String.valueOf(score));
            if(dragon_fly_by_count >= 15) {
                dragon_fly_by = false;
                dragonAnimation.stop();
            }
            if(!dragon_coming && !dragon_fly_by){
                if(RANDOM.nextInt(50) == 3){
                    dragon_coming = true;
                    warningtxt.setText("Dragon Coming! HIDE");
                    timerHandler.postDelayed(timerWarning, 1500);
                }
                else dragon_coming = false;
            }
            if(progress_running == Distance_to_run){
                initializeNextGame();
                finish();
            }

            timerHandler.postDelayed(this, 100);
        }
    };



    public PerilousJourneyActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }
    public PerilousJourneyActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perilous_journey);

        startWatchActivity();

        first_time = true;
        dragon_fly_by = false;

        receiveLastGameData();

        maxText = findViewById(R.id.distanceGoalTextView);
        maxText.setText(String.valueOf(Distance_to_run));
        progresstxt = findViewById(R.id.distanceNowTextView);
        warningtxt = findViewById(R.id.warningTextView);
        scoreText = findViewById(R.id.scoreTextView);

        distanceRun = findViewById(R.id.progressBar);
        distanceRun.setMax(Distance_to_run);
        distanceRun.setProgress(0);
        distanceRun.setRotation(180);

        score = 300;

        dragonView = (ImageView) findViewById(R.id.dragonImageView);
        dragonView.setBackgroundResource(R.drawable.animation_dragon);
        dragonAnimation = (AnimationDrawable) dragonView.getBackground();

        playerView = (ImageView) findViewById(R.id.playerImageView);
        runningAnimation = (AnimationDrawable) playerView.getBackground();
        boxAnimation = (AnimationDrawable) playerView.getBackground();

        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void startWatchActivity() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.PERILOUSJOURNEY.name());
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lux_value = intent.getFloatExtra(LUXVALUE,10000);
                if(lux_value < 50) {
                    if(first_time){
                        playerView.setBackgroundResource(R.drawable.animation_box);
                        boxAnimation = (AnimationDrawable) playerView.getBackground();
                        runningAnimation = (AnimationDrawable) playerView.getBackground();
                        boxAnimation.start();
                        first_time = false;
                    }
                    else if(is_running && !first_time) {
                        runningAnimation.stop();
                        playerView.setBackgroundResource(R.drawable.animation_box);
                        boxAnimation = (AnimationDrawable) playerView.getBackground();
                        boxAnimation.start();
                        is_running = false;
                    }
                }
                else {
                    if(first_time){
                        playerView.setBackgroundResource(R.drawable.animation_running);
                        runningAnimation = (AnimationDrawable) playerView.getBackground();
                        boxAnimation = (AnimationDrawable) playerView.getBackground();
                        runningAnimation.start();
                        first_time = false;
                    }
                    else if(!is_running && !first_time){
                        boxAnimation.stop();
                        playerView.setBackgroundResource(R.drawable.animation_running);
                        runningAnimation = (AnimationDrawable) playerView.getBackground();
                        runningAnimation.start();
                        is_running = true;
                    }
                }
            }
        }, new IntentFilter(SENDINGLUXVALUE));
    }
}

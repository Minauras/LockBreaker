package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class FinalScreenActivity extends MiniGame {

    public static final String RESTART_TAG = "RESTART_TAG";

    private TextView mScoreTextView;
    private TextView mMessageTextView;

    private Button mMenuButton;
    private Button mPlayAgainButton;


    public FinalScreenActivity(List<Class> gameActivity, int totscore, int difficulty, int gameStatus) {
        super(gameActivity, totscore, difficulty, gameStatus); }

    public FinalScreenActivity(){ super(); }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);

        Log.d("NICOLAS", "HEY");

        mScoreTextView = (TextView) findViewById(R.id.scoreTextView);
        mMessageTextView = (TextView) findViewById(R.id.messageTextView);

        mMenuButton = (Button) findViewById(R.id.menuButton);
        mPlayAgainButton = (Button) findViewById(R.id.playAgainButton);
        Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicGameService.class);
        stopService(intentmusic);

        receiveLastGameData();

        String message;

        Log.e("NICOLAS", String.valueOf(gameLost));


        if(gameLost == 1){
            message = "You Lost! :(";
        }else {
            if (totalScore > 50) {
                message = "You're good !";
            } else if (totalScore > 10) {
                message = "Nice Work you two";
            } else {
                message = "You really need to learn to communicate :/";
            }
        }
        UserProfile user = new UserProfile();
        boolean best = user.updateScore(totalScore);
        Log.d("NICOLAS", "HEY Here " + totalScore);
        if (best){
            message = "NEW BEST SCORE !!!!";
        }

        mScoreTextView.setText(String.valueOf(totalScore));
        mMessageTextView.setText(message);

        //start the watch on the main screen
        sendStart();
    }

    public void goToMenu(View view) {
        Intent toMenu = new Intent(FinalScreenActivity.this, StartGameActivity.class);
        startActivity(toMenu);
        finish();
    }

    public void playAgain(View view) {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra(RESTART_TAG, "RESTART");
        startActivity(intent);
        finish();
    }

    public void sendStart() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STARTACTIVITY.name());
        intent.putExtra(WearService.ACTIVITY_TO_START, BuildConfig.W_end_screen);
        startService(intent);
    }

}

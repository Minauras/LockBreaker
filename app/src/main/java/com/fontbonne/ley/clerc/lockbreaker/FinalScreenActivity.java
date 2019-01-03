package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
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


    private TextView mScoreTextView;
    private TextView mMessageTextView;

    private Button mMenuButton;
    private Button mPlayAgainButton;


    public FinalScreenActivity(List<Class> gameActivity, int totscore) { super(gameActivity, totscore); }

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

        receiveLastGameData();

        String message;
        //totalScore = 19;

        Log.e("NICOLAS", "HEY Here " + totalScore);


        if (totalScore > 50){
            message = "You're good !";
        } else if (totalScore > 10){
            message = "Nice Work you two";
        } else {
            message = "You really need to learn to communicate :/";
        }

        Log.d("NICOLAS", "HEY Here " + totalScore);

        mScoreTextView.setText(String.valueOf(totalScore));
        mMessageTextView.setText(message);

    }

    public void goToMenu(View view) {
        Intent toMenu = new Intent(FinalScreenActivity.this, StartGameActivity.class);
        startActivity(toMenu);
    }

    public void playAgain(View view) {

    }
}

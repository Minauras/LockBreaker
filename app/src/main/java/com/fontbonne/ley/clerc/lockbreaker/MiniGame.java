package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/***********************************************************************************************
 *
 *  MiniGame SuperClass
 *
 ***********************************************************************************************/
public class MiniGame extends AppCompatActivity {
    // General Score -------------------------------------------------------------------------------
    protected int totalScore;
    protected int score;

    // startActivity (to be overwritten) -----------------------------------------------------------
    protected void startGame(Context context){}

    // Score Manipulation --------------------------------------------------------------------------
    protected void setScore(int s){score = s;} //only temporary

    protected void updateScore() {
        totalScore = score + totalScore;
    }

    protected int getTotalScore() {return totalScore; }
}

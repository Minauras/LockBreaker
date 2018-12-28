package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/***********************************************************************************************
 *
 *  MiniGame SuperClass
 *
 ***********************************************************************************************/
public class MiniGame extends AppCompatActivity implements Parcelable{

    // General Score -------------------------------------------------------------------------------
    protected List<Class> games;
    protected Class nextGame;

    // General Score -------------------------------------------------------------------------------
    protected int totalScore;
    protected int score;

    // Constructor ---------------------------------------------------------------------------------
    public MiniGame(List<Class> gameList) {
        games = gameList;
        nextGame = null;
        totalScore = 10;
        score = 0;
    }

    public MiniGame(){
        games = new ArrayList<Class>();
        nextGame = null;
        totalScore = 0;
        score = 0;
    }

    protected MiniGame copy(){
        MiniGame copied = new MiniGame();
        copied.games = games;
        copied.totalScore = totalScore;
        return copied;
    }

    // Methods to be overwritten by sub-classes ----------------------------------------------------
    protected void startGame(Context context){};
    protected void findNext(){};

    // Parceable -----------------------------------------------------------------------------------

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeList(games);
        out.writeInt(totalScore);
    }

    public static final Parcelable.Creator<MiniGame> CREATOR
            = new Parcelable.Creator<MiniGame>() {
        public MiniGame createFromParcel(Parcel in) {
            return new MiniGame(in);
        }

        public MiniGame[] newArray(int size) {
            return new MiniGame[size];
        }
    };

    private MiniGame(Parcel in) {
        List<Class> temp = new ArrayList<Class>();
        in.readList(temp, null);
        games = temp;
        totalScore = in.readInt();
    }

    // Getter and Setters --------------------------------------------------------------------------
    protected List<Class> getGames(){return games;}

    protected void setScore(int s){score = s;} //only temporary

    protected void updateScore() {
        totalScore = score + totalScore;
    }

    protected int getTotalScore() {return totalScore; }
}

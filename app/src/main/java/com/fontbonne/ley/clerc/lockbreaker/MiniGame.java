package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    // Call this method to pass to next mini-game --------------------------------------------------
    protected void initializeNextGame(){
        findNext();
        Constructor<?> nextGameConstructor = null;
        Object nextGameObj = null;
        Method nextGameMethod = null;
        try {
            nextGameConstructor = nextGame.getConstructor(List.class);
            nextGameObj = nextGameConstructor.newInstance(new Object[] { games });
            nextGameMethod = nextGameObj.getClass().getMethod("startGame", Context.class);
            nextGameMethod.invoke(nextGameObj, getApplicationContext());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("ERROR InitGame", "IllegalAccessException");
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.e("ERROR InitGame", "InstantiationException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e("ERROR InitGame", "InvocationTargetException");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e("ERROR InitGame", "NoSuchMethodException");
        }
    }

    // Methods used in initializeNextGame ----------------------------------------------------
    protected void startGame(Context context){
        Intent intent = new Intent(context, this.getClass());
        Bundle bundle = new Bundle();
        Log.e("TAG_Patrick", String.valueOf(this));
        bundle.putParcelable("data", this);
        intent.putExtras(bundle);
        context.startActivity(intent);
    };
    protected void findNext(){
        if(games.contains(this.getClass())){
            int ind = games.indexOf(this.getClass());
            nextGame = games.get(ind + 1);
        }else {
            Log.e("TAG_Patrick", "ERROR: not in games list");
        }
    };

    // Call this method in onCreate to get the intent data -----------------------------------------
    protected void receiveLastGameData(){
        Bundle bundle = getIntent().getExtras();
        MiniGame game = new MiniGame();
        game = bundle.getParcelable("data");

        games = game.games;
        totalScore = game.totalScore;
    }

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

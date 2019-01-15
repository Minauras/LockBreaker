package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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

    // Difficulty -------------------------------------------------------------------------------
    protected int difficulty = 1;

    // Constructor ---------------------------------------------------------------------------------
    public MiniGame(List<Class> gameList, int TotScore) {
        games = gameList;
        nextGame = null;
        totalScore = TotScore;
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

        updateScore();
        findNext();
        Constructor<?>[] nextGameConstructor = null;
        Object nextGameObj = null;
        Method nextGameMethod = null;
        Log.e("TAG_PAT", "totalScore" + totalScore);
        try {

            nextGameConstructor = nextGame.getConstructors();
            nextGameObj = nextGameConstructor[1].newInstance(new Object[] { games, totalScore });
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

    // Methods to use to communicate with the WearOS------------------------------------------------

    public void sendStart(View view) {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STARTACTIVITY.name());
        intent.putExtra(WearService.ACTIVITY_TO_START, BuildConfig.W_mainactivity);
        startService(intent);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.MESSAGE.name());
        intent.putExtra(WearService.MESSAGE, "Messaging other device!");
        intent.putExtra(WearService.PATH, BuildConfig.W_example_path_text);
        startService(intent);
    }

    // Methods used in initializeNextGame ----------------------------------------------------------
    protected void startGame(Context context){
        Intent intent = new Intent(context, this.getClass());
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", this);
        Log.e("TAG_Patrick", String.valueOf(bundle));
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

    protected void addtoScore(int s){score += s;} //only temporary

    protected void updateScore() {
        totalScore = score + totalScore;
    }

    protected int getTotalScore() {return totalScore; }
}

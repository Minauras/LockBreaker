package com.fontbonne.ley.clerc.lockbreaker;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class BackgroundTimerService extends Service {

    int sec;
    int min;

    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if(updateTime()) {
                Intent localIntent = new Intent(MiniGame.UPDATETIMER);
                                // Puts the status into the Intent
                localIntent.putExtra(MiniGame.MIN, min);
                localIntent.putExtra(MiniGame.SEC, sec);
                // Broadcasts the Intent to receivers in this app.
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    private boolean updateTime() {
        sec -= 1;
        if(sec == 0 && min < 1){
            min = 0;
            timeUp();
            return false;
        }
        else if(sec < 0){
            sec = 59;
            min -= 1;
        }
        return true;
    }

    private void timeUp() {
        Intent localIntent = new Intent(MiniGame.TIMEUP);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    @Override
    public void onCreate() {
        Log.e("PAT_LOG", "onCreate MUSIC");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get the mins and secs when started
        min = intent.getIntExtra(MiniGame.MIN, -1);
        sec = intent.getIntExtra(MiniGame.SEC, -1);
        if(min != -1 || sec != -1){
            timerHandler.postDelayed(timerRunnable, 0);
        }
        else{
            Log.e("PAT_LOG", "ERROR Service started but not initialized correctly");
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("TAG_PAT", "onDESTROY");
        timerHandler.removeCallbacks(timerRunnable);
    }
}
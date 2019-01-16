package com.fontbonne.ley.clerc.lockbreaker;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BackgroundMusicGameService extends Service {
    MediaPlayer player;

    @Override
    public void onCreate() {
        Log.e("PAT_LOG", "onCreate MUSIC");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("PAT_LOG", "onStart MUSIC");
        player = MediaPlayer.create(this, R.raw.lockbreakergame);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        player.start();
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        player.stop();
    }
}

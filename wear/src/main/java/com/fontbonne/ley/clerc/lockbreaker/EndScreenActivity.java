package com.fontbonne.ley.clerc.lockbreaker;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.view.MotionEvent;
import android.view.View;

public class EndScreenActivity extends WearableActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_end_screen);

    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

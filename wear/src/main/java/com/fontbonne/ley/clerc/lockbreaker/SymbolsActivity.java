package com.fontbonne.ley.clerc.lockbreaker;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wear.ambient.AmbientModeSupport;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SymbolsActivity extends WearableActivity{

    public static final String START_SYMBOLS = "START_SYMBOLS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbols);


        Intent intent = getIntent();
        String data = intent.getStringExtra(START_SYMBOLS);

        initLabels(data);
    }

    private void initLabels(String data) {
        for (Integer i = 1; i <= 4; i++) {
            String textViewID = "textView" + i.toString();
            int resID = getResources().getIdentifier(textViewID, "id", getPackageName());
            TextView textView = findViewById(resID);
            String value = String.valueOf(data.charAt(i - 1));
            textView.setText(value);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

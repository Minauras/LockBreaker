package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class WaldoProfileActivity extends WearableActivity {

    private TextView mTextView;
    private CharacterView mCharacter;
    private ViewGroup background;

    public static final String WALDO_NAME = "WALDO_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waldo_profile);

        Log.d("NICO","WaldoProfileActivity");
        Intent intent = getIntent();
        String name = intent.getStringExtra(WALDO_NAME);

        mTextView = (TextView) findViewById(R.id.text);
        background = (ViewGroup) findViewById(R.id.background);
        mCharacter = new CharacterView(this, 10, 10,0.5F , name, true);
        background.addView(mCharacter);
        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

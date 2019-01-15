package com.fontbonne.ley.clerc.lockbreaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StepByStep extends WearableActivity {

    Button stepbtn;
    boolean my_turn;
    int step;

    public static final String STEPCOUNT = "STEPCOUNT";
    public static final String STEPINTENTWEAR = "STEPINTENTWEAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step);

        stepbtn = findViewById(R.id.stepBtn);
        stepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(my_turn){
                    my_turn = false;
                    step += 1;
                }else{
                    step = 1;
                }
                Log.e("TAG_PAT", "Wear btn pressed Step Count: " + String.valueOf(step));
                updateStepCount();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                my_turn = true;
                step = intent.getIntExtra(STEPCOUNT,0);
                Log.e("TAG_PAT", "Wear msg received Step Count: " + String.valueOf(step));
            }
        }, new IntentFilter(STEPINTENTWEAR));

        // Enables Always-on
        setAmbientEnabled();
    }

    private void updateStepCount() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STEP.name());
        intent.putExtra(WearService.STEPCOUNT, step);
        startService(intent);
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

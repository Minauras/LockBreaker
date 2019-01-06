package com.fontbonne.ley.clerc.lockbreaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

public class PerilousJourney extends WearableActivity implements SensorEventListener {

    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    public static final String STARTACTIVITY = "STARTACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perilous_journey);

        mTextView = (TextView) findViewById(R.id.text);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("WATCHTAGS", "SETTING");
                mTextView.setText("Button Clicked");
            }
        }, new IntentFilter(STARTACTIVITY));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Enables Always-on
        setAmbientEnabled();
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            // Success! There's a TYPE_ROTATION_VECTOR sensor.
            if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
                float lux = sensorEvent.values[0];
                sendLux(lux);
                if(mTextView != null) mTextView.setText(String.valueOf(lux));
            }
        }
    }

    public void sendLux(float lux) {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.LUX.name());
        intent.putExtra(WearService.LUX, lux);
        intent.putExtra(WearService.PATH, BuildConfig.W_lux);
        startService(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

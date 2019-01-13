package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class SpaceWord extends WearableActivity implements SensorEventListener {

    //private TextView mTextView;
    //private TextView mTextView1;
    //private TextView mTextView2;
    private TextView letterTextView;

    boolean firstTime = true;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private final float[] mRotationMatrix = new float[16];
    private float[] orientation = new float[3];
    private float[] quaternion = new float[4];
    private float azimuth;
    private float pitch;
    private float roll;
    private static double MARGIN = PI/2;
    private static double ZEROMARGIN = 0.2;
    private String answer;
    private String[] letters;
    private static final Random RANDOM = new Random();
    private boolean answerReceived = false;
    List<COLORS> arr_colors = new ArrayList<COLORS>(){{
        add(COLORS.RED);
        add(COLORS.BLUE);
        add(COLORS.YELLOW);
        add(COLORS.GREEN);
        add(COLORS.CYAN);
        add(COLORS.WHITE);
        add(COLORS.MAGENTA);
    }};

    public static final String STARTACTIVITY = "STARTACTIVITY";
    public static final String ANSWER = "ANSWER";

    private enum COLORS {RED, BLUE, YELLOW, GREEN, CYAN, WHITE, MAGENTA};

    class Letter
    {
        public String letter;
        public double x;
        public double y;
        public double z;
        public int colorInd;

        public Letter(){

        }

        public Letter(Letter another) {
            this.letter = another.letter;
            this.x = another.x;
            this.y = another.y;
            this.z = another.z;
            this.colorInd = another.colorInd;
        }
    };

    private List<Letter> listLetters = new ArrayList<Letter>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_word);

        Log.d("NICO", "SpaceWord");

        letterTextView = findViewById(R.id.lettertextview);

        Intent intent = getIntent();
        answer = intent.getStringExtra(ANSWER);
        answerReceived = true;
        letters = answer.split("(?!^)");

        Collections.shuffle(arr_colors);

        int i = 0;
        for (String l : letters) {
            Letter newLetter = new Letter();
            newLetter.letter = l;
            i += 1;
            if(RANDOM.nextBoolean())newLetter.x = RANDOM.nextDouble();
            else newLetter.x = -RANDOM.nextDouble();
            if(RANDOM.nextBoolean())newLetter.y = RANDOM.nextDouble();
            else newLetter.y = -RANDOM.nextDouble();
            if(RANDOM.nextBoolean())newLetter.z = RANDOM.nextDouble();
            else newLetter.z = -RANDOM.nextDouble();
            newLetter.colorInd = i;
            listLetters.add(newLetter);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

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


    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null){
            // Success! There's a TYPE_ROTATION_VECTOR sensor.
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR && answerReceived) {

                double dist = 10;
                double mindist = 10000;
                Letter minLetter = new Letter();
                for (Letter l: listLetters) {
                    if(firstTime) {
                        Log.e("TAG_PAT",l.letter + "\n X: " + l.x + " Y: " + l.y + " Z: " + l.z);
                    }
                    dist = abs(event.values[0] - l.x)+abs(event.values[1] - l.y)+abs(event.values[2] - l.z);
                    if(mindist > dist) {
                        mindist = dist;
                        minLetter = new Letter(l);
                    }
                }

                if(letterTextView != null) {
                    letterTextView.setText(minLetter.letter);
                    int color = findColor(minLetter.colorInd);
                    letterTextView.setTextColor(color);
                }
                firstTime = false;
            }
        } else {
            Log.e("TAG_PA", "Failure! No pressure sensor.");
        }

    }

    private int findColor(int i) {
        switch (arr_colors.get(i)){
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case CYAN:
                return Color.CYAN;
            case WHITE:
                return Color.WHITE;
            case MAGENTA:
                return Color.MAGENTA;
        }
        return Color.RED;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

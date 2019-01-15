package com.fontbonne.ley.clerc.lockbreaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MisleadingColors extends WearableActivity {

    public enum COLOR{
        RED, BLUE, GREEN, YELLOW
    }


    public static final String MISLEADINGCOLORS = "MISLEADINGCOLORS";
    public static final String MISLEADINGCOLORS_ARRAYLIST = "MISLEADINGCOLORS_ARRAYLIST";


    private TextView ColorsTL;
    private TextView ColorsTR;
    private TextView ColorsBL;
    private TextView ColorsBR;
    private COLOR[] colorOFtext = new COLOR[]{COLOR.RED, COLOR.RED, COLOR.RED, COLOR.RED};
    private COLOR[] colorOfcolor = new COLOR[]{COLOR.RED, COLOR.RED, COLOR.RED, COLOR.RED};
    private ArrayList<Integer> arr_col;
    private static final Random RANDOM = new Random();
    private static final List<COLOR> VALUES =
            Collections.unmodifiableList(Arrays.asList(COLOR.values()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misleading_colors);

        Log.d("NICO", "MisleadingColors");

        Intent intent = getIntent();
        arr_col = intent.getIntegerArrayListExtra(MISLEADINGCOLORS_ARRAYLIST);
        for(int i=0; i<8; i++) {
            if ((i % 2) == 0)
                colorOfcolor[i/2] = convertfrom(arr_col.get(i));
            else
                colorOFtext[(i-1)/2] = convertfrom(arr_col.get(i));
        }

        ColorsTL = (TextView) findViewById(R.id.colorTopLeft);
        ColorsTR = (TextView) findViewById(R.id.colorTopRight);
        ColorsBL = (TextView) findViewById(R.id.colorBotLeft);
        ColorsBR = (TextView) findViewById(R.id.colorBotRight);

        setColor(ColorsTL, colorOfcolor[0]);
        setText(ColorsTL, colorOFtext[0]);
        setColor(ColorsTR, colorOfcolor[1]);
        setText(ColorsTR, colorOFtext[1]);
        setColor(ColorsBL, colorOfcolor[2]);
        setText(ColorsBL, colorOFtext[2]);
        setColor(ColorsBR, colorOfcolor[3]);
        setText(ColorsBR, colorOFtext[3]);


        // Enables Always-on
        setAmbientEnabled();
    }

    private void setColor(TextView text, COLOR color){
        switch (color){
            case RED:
                text.setTextColor(Color.RED);
                break;
            case BLUE:
                text.setTextColor(Color.BLUE);
                break;
            case GREEN:
                text.setTextColor(Color.GREEN);
                break;
            case YELLOW:
                text.setTextColor(Color.YELLOW);
                break;
            default:
                Log.e("TAG_PAT", "ERROR THIS COLOR NOT KNOWN");
        }
    }

    private COLOR convertfrom(int i){
        switch (i){
            case 0:
                return COLOR.RED;
            case 1:
                return COLOR.BLUE;
            case 2:
                return COLOR.GREEN;
            case 3:
                return COLOR.YELLOW;
            default:
                Log.e("TAG_PAT", "ERROR THIS COLOR NOT KNOWN");
        }
        return COLOR.RED;
    }

    private void setText(TextView text, COLOR color){
        switch (color){
            case RED:
                text.setText("Red");
                break;
            case BLUE:
                text.setText("Blue");
                break;
            case GREEN:
                text.setText("Green");
                break;
            case YELLOW:
                text.setText("Yellow");
                break;
            default:
                Log.e("TAG_PAT", "ERROR THIS COLOR NOT KNOWN");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

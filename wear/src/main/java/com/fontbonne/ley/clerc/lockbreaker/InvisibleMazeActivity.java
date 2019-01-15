package com.fontbonne.ley.clerc.lockbreaker;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class InvisibleMazeActivity extends WearableActivity {

    MazeView maze;

    public static final String MAZE_CELLS = "MAZE_CELLS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invisible_maze);


        Intent intent = getIntent();
        String cellStream = intent.getStringExtra(MAZE_CELLS);

        maze = findViewById(R.id.maze);
        maze.setVisible(true);
        maze.setVisibleMovements(false);


        maze.decodeCellStream(cellStream);
        maze.refresh();
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

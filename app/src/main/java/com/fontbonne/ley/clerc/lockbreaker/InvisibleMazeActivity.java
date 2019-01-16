package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class InvisibleMazeActivity extends MiniGame {

    MazeView maze;
    private TextView time;

    //constructors
    public InvisibleMazeActivity(List<Class> gameActivity, int totscore, int difficulty, int gameStatus) {
        super(gameActivity, totscore, difficulty, gameStatus);
    }

    public InvisibleMazeActivity() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invisible_maze);

        time = findViewById(R.id.timeView);

        receiveLastGameData();
        Log.d("DIFFICULTY", String.valueOf(difficulty));
        switch (difficulty){
            case 0:
                //easy
                score = 100;
                break;
            case 1:
                // medium
                score = 200;
                break;
            case 2:
                // hard
                score = 300;
                break;
        }

        maze = findViewById(R.id.maze);
        maze.setDirected(false);
        maze.setVisible(false);
        maze.setVisibleMovements(true);


        //send the maze to the watch
        String maze_cells = maze.createCellStream();

        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.INVISIBLE_MAZE.name());
        intent.putExtra(WearService.MAZE_CELLS, maze_cells);
        startService(intent);
    }

    public void moveUp(View view) {
        maze.movePlayer(MazeView.Direction.TOP);
        if(maze.checkWin()) {
            initializeNextGame();
            finish();
        }
    }

    public void moveDown(View view) {
        maze.movePlayer(MazeView.Direction.BOTTOM);
        if(maze.checkWin()) {
            initializeNextGame();
            finish();
        }
    }

    public void moveLeft(View view) {
        maze.movePlayer(MazeView.Direction.LEFT);
        if(maze.checkWin()) {
            initializeNextGame();
            finish();
        }
    }

    public void moveRight(View view) {
        maze.movePlayer(MazeView.Direction.RIGHT);
        if(maze.checkWin()) {
            initializeNextGame();
            finish();
        }
    }

    @Override
    public void callbackTimer(){
        if(time != null){
            if(min_cur == 0) time.setTextColor(Color.RED);
            time.setText(Integer.toString(min_cur) + ":" + Integer.toString(sec_cur));
        }
    }
    @Override
    protected void onPause() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity

            Log.e("TAG_PAT", "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY");
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicGameService.class);
                stopService(intentmusic);
                Log.e("TAG_PAT", "YOU LEFT YOUR APP");
            }
            else {
                Log.e("TAG_PAT", "YOU SWITCHED ACTIVITIES WITHIN YOUR APP");
            }
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity

            Log.e("TAG_PAT", "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY");
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Log.e("TAG_PAT", "YOU LEFT YOUR APP");
            }
            else {
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicGameService.class);
                startService(intentmusic);
                Log.e("TAG_PAT", "YOU SWITCHED ACTIVITIES WITHIN YOUR APP");
            }
        }
        super.onResume();
    }
}
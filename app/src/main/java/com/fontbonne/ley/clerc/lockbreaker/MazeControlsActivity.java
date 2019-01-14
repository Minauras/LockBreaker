package com.fontbonne.ley.clerc.lockbreaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MazeControlsActivity extends MiniGame {

    public static final String WEAR_MAZE_CONTROL = "WEAR_MAZE_CONTROL";
    public static final String TURN_DATA = "TURN_DATA";
    MazeView maze;

    //constructors
    public MazeControlsActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }

    public MazeControlsActivity() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_controls);

        receiveLastGameData();

        maze = findViewById(R.id.maze);
        maze.setDirected(true);

        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.MAZE_CONTROLS.name());
        startService(intent);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = intent.getStringExtra(TURN_DATA);

                if(data.equals("turn_left")){
                    maze.rotatePlayer(MazeView.Rotation.COUNTERCLOCKWISE);
                }
                else if(data.equals("turn_right")){
                    maze.rotatePlayer(MazeView.Rotation.CLOCKWISE);
                }
            }
        }, new IntentFilter(WEAR_MAZE_CONTROL));

    }

    public void forwardButtonCallback(View view) {
        maze.movePlayer(MazeView.Direction.TOP);
        if(maze.checkWin()) {
            initializeNextGame();
        }
    }
}
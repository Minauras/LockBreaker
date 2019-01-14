package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class InvisibleMazeActivity extends MiniGame {

    MazeView maze;


    //constructors
    public InvisibleMazeActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }

    public InvisibleMazeActivity() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invisible_maze);

        receiveLastGameData();

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
        }
    }

    public void moveDown(View view) {
        maze.movePlayer(MazeView.Direction.BOTTOM);
        if(maze.checkWin()) {
            initializeNextGame();
        }
    }

    public void moveLeft(View view) {
        maze.movePlayer(MazeView.Direction.LEFT);
        if(maze.checkWin()) {
            initializeNextGame();
        }
    }

    public void moveRight(View view) {
        maze.movePlayer(MazeView.Direction.RIGHT);
        if(maze.checkWin()) {
            initializeNextGame();
        }
    }

}
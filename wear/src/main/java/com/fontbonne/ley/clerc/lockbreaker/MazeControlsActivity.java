package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MazeControlsActivity extends WearableActivity {

    private static final String WEAR_MAZE_CONTROL = "WEAR_MAZE_CONTROL";
    private static final String TURN_DATA = "TURN_DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_controls);

        Intent intent = getIntent();

        Button leftButton = findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTurn("turn_left");
            }
        });

        Button rightButton = findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTurn("turn_right");
            }
        });
    }

    private void sendTurn(String message) {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.MAZE_CONTROLS.name());
        intent.putExtra(WearService.MESSAGE, message);
        startService(intent);
    }

}

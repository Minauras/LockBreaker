package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartGameActivity extends AppCompatActivity {

    private Button mStartButton;
    private Button mOptionButton;
    private Button mStatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        mStartButton = findViewById(R.id.playButton);
        mOptionButton = findViewById(R.id.optionButton);
        mStatButton = findViewById(R.id.statButton);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartGameActivity.this, "START", Toast.LENGTH_LONG).show();

                MisleadingColorsActivity activity1 = new MisleadingColorsActivity();
                activity1.startGame(StartGameActivity.this);
            }
        });

        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statIntent = new Intent(StartGameActivity.this, OptionActivity.class);
                startActivity(statIntent);
            }
        });

        mStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionIntent = new Intent(StartGameActivity.this, StatActivity.class);
                startActivity(optionIntent);
            }
        });
    }
}

package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartGameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStartButton;
    private Button mOptionButton;
    private Button mStatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.playButton:
                // Play Game
                break;
            case R.id.statButton:
                Intent statIntent = new Intent(StartGameActivity.this, StatActivity.class);
                startActivity(statIntent);
                break;
            case R.id.optionButton:
                Intent optionIntent = new Intent(StartGameActivity.this, OptionActivity.class);
                startActivity(optionIntent);
                break;
        }
    }
}

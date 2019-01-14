package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class StartGameActivity extends MiniGame{

    private Button mStartButton;
    private Button mOptionButton;
    private Button mStatButton;

    private static int NBRMINIGAMES = 1;
    private int nbrGames = 1;

    public StartGameActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }
    public StartGameActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        mStartButton = findViewById(R.id.playButton);
        mOptionButton = findViewById(R.id.optionButton);
        mStatButton = findViewById(R.id.statButton);

        setupGame();

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartGameActivity.this, "START", Toast.LENGTH_LONG).show();
                initializeNextGame();
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

    private void setupGame() {
        // Add all minigames here to games

        //games.add(WaldoActivity.class);

        games.add(SimilarQuizActivity.class);
        games.add(MisleadingColorsActivity.class);
        games.add(SpaceWordActivity.class);
        games.add(PerilousJourneyActivity.class);
        games.add(StepByStepActivity.class);
        games.add(SymbolsActivity.class);
        games.add(MazeControlsActivity.class);
        games.add(InvisibleMazeActivity.class);
        games.add(EncryptedActivity.class);

        // Randomize the list of games
        Collections.shuffle(games);

        // make games be the size of nbrGames
        if(nbrGames < NBRMINIGAMES)
            for(int i = 0; i<(NBRMINIGAMES-nbrGames);i++)
                games.remove(games.size() - 1);

        // Add starting screen and final screen
        games.add(0,StartGameActivity.class);
        games.add(FinalScreenActivity.class);
    }
}

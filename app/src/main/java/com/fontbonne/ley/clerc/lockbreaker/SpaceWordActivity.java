package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceWordActivity extends MiniGame {

    public SpaceWordActivity(List<Class> gameActivity, int totscore, int difficulty) {
        super(gameActivity, totscore, difficulty);
    }
    public SpaceWordActivity() {
        super();
    }

    private Button btnCheck;
    private EditText editText;
    private TextView nbrLettersText;

    private String answer;
    String[] letters;
    private String input;
    private int nbrGamesPlayed;
    private static final Random RANDOM = new Random();

    // should change this to a database!
    private List<String> database = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_word);

        editText = findViewById(R.id.editText);
        btnCheck = findViewById(R.id.button);
        nbrLettersText = findViewById(R.id.nbrLettersTextView);

        receiveLastGameData();
        Log.d("DIFFICULTY", String.valueOf(difficulty));
        switch (difficulty){
            case 0:
                //easy
                break;
            case 1:
                // medium
                break;
            case 2:
                // hard
                break;
        }
        databaseStuff();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = editText.getText().toString();
                if(input != null) {
                    if (answer.equals(input)) {
                        Log.e("TAG_RESULTS", "THIS IS CORRECT!!");
                        Toast.makeText(SpaceWordActivity.this,
                                "THIS IS CORRECT!!", Toast.LENGTH_SHORT).show();
                        addtoScore(100);
                    } else {
                        Log.e("TAG_RESULTS", "THIS IS WRONG! Answer is " + answer);
                        Toast.makeText(SpaceWordActivity.this,
                                "THIS IS WRONG!", Toast.LENGTH_SHORT).show();
                    }
                    nbrGamesPlayed++;
                    if (nbrGamesPlayed < 3) setupGame();
                    else{
                        initializeNextGame();
                        finish();
                    }
                }else {
                    Toast.makeText(SpaceWordActivity.this,
                            "Input a word to continue!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setupGame();
    }

    private void setupGame(){
        int index = RANDOM.nextInt(database.size());
        answer = database.get(index);
        database.remove(index);
        editText.setText("");
        nbrLettersText.setText("(" + answer.length() + "Letters)");
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.SPACEWORD.name());
        intent.putExtra(WearService.MESSAGE, answer);
        intent.putExtra(WearService.PATH, BuildConfig.W_space_word_answer);
        startService(intent);
    }

    private void databaseStuff() {
        database.add("Hello");
        database.add("Dark");
        database.add("Friend");
        database.add("Again");
        database.add("Siesta");
        database.add("Garage");
    }

}

package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EncryptedActivity extends MiniGame {

    private ArrayList<Character> symbols = new ArrayList<>();
    private ArrayList<Integer> solution = new ArrayList<>();
    private ArrayList<Character> solution_symbol = new ArrayList<>();
    private ArrayList<Character> letters = new ArrayList<>();

    private TextView time;

    private int current_score = 300;

    MediaPlayer playerwrong;

    //constructors
    public EncryptedActivity(List<Class> gameActivity, int totscore, int difficulty, int gameStatus) {
        super(gameActivity, totscore, difficulty, gameStatus);
    }

    public EncryptedActivity() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted);

        playerwrong = MediaPlayer.create(this, R.raw.wrongsfx);

        //receive last game data
        receiveLastGameData();

        switch (difficulty) {
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

        setupGame();


    }

    private void setupGame() {
        setContentView(R.layout.activity_encrypted);
        time = findViewById(R.id.timeView);

        symbols.clear();
        solution.clear();
        solution_symbol.clear();
        letters.clear();

        //give each button a random, non-repeated symbol, out of the symbol list
        String chosenWord = chooseWord();
        TextView instructionsLabel = (TextView) findViewById(R.id.instructionsLabel);
        instructionsLabel.setText("Code word: " + chosenWord);

        //transmit the lookup table to the watch and start the activity
        transmitTable();

        initButtons();

    }

    private void initButtons() {
        //first, shuffle the entries
        Collections.shuffle(symbols);

        for (Integer i = 1; i <= 16; i++) {
            String buttonID = "button" + i.toString();
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = findViewById(resID);
            String value = String.valueOf(symbols.get(i - 1));
            button.setText(value);
        }
    }

    private void transmitTable() {
        //cast symbols to string for transmission
        StringBuilder symbols_string = new StringBuilder(symbols.size());
        for (Character c : symbols) {
            symbols_string.append(c);
            symbols_string.append("%");
        }
        String symbols_string_t = symbols_string.toString();

        //cast letters to string for transmission
        StringBuilder letters_string = new StringBuilder(letters.size());
        for (Character c : letters) {
            letters_string.append(c);
            letters_string.append("%");
        }
        String letters_string_t = letters_string.toString();

        //concatenate both to transmit all at once
        String data = symbols_string_t + letters_string_t;

        //transmit and start activity
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.ENCRYPTED.name());
        intent.putExtra(WearService.ENCRYPTED_DATA, data);
        startService(intent);
    }


    private String chooseWord() {
        Word chosenWord = new Word(difficulty);
        String word = chosenWord.word;
        int number_symbols = word.length();

        //choose the symbols corresponding to each letter randomly
        for (int i = 0; i < number_symbols; i++) {
            Random r = new Random();
            int index = r.nextInt(23);

            while (solution.contains(index)) {
                index = r.nextInt(23);
            }

            solution.add(index); //add index to list of already chosen indices
            solution_symbol.add(getResources().getString(R.string.symbols).charAt(index)); //keep track of the solution
            symbols.add(getResources().getString(R.string.symbols).charAt(index)); //also add them to the list that will be displayed
            letters.add(word.charAt(i)); //add the letters corresponding to the symbols

        }

        //complete the list of symbols to be displayed on the keyboard
        for (int i = number_symbols; i < 16; i++) {
            Random r = new Random();
            int index = r.nextInt(23);

            while (solution.contains(index)) {
                index = r.nextInt(23);
            }

            symbols.add(getResources().getString(R.string.symbols).charAt(index)); //add them to the list that will be displayed
            solution.add(index);

            index = r.nextInt(26);
            Character letter = getResources().getString(R.string.alphabet).charAt(index);
            while (letters.contains(letter)) {
                index = r.nextInt(26);
                letter = getResources().getString(R.string.alphabet).charAt(index);
            }
            letters.add(letter); //add corresponding bogus letters
        }

        return word;
    }

    public void check_symbol(View view) {
        Button button = findViewById(view.getId());

        char chosenSymbol = button.getText().charAt(0);
        if (solution_symbol.get(0).equals(chosenSymbol)) { //check if symbol is correct in order

            //if answer is correct, remove answer from solution
            solution_symbol.remove(0);

            //turn button green
            button.setBackgroundColor(Color.GREEN);

            if (solution_symbol.isEmpty()) {
                //game is won
                score = current_score;
                initializeNextGame();
                finish();
            }
        } else {
            //if answer is false, turn button red
            button.setBackgroundColor(Color.RED);
            button.setEnabled(false);

            //decrease score
            current_score -= 75;

            //restart
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playerwrong.start();
                    //Do something after 500ms
                    setupGame();
                }
            }, 500);
        }
    }


    private class Word {
        String word;

        public Word(int difficulty) {
            Random rand = new Random();
            String json = loadJSONFromAsset(EncryptedActivity.this, difficulty);
            try {
                JSONObject reader = new JSONObject(json);
                int randCeiling = reader.getInt("nb");

                int index = rand.nextInt(randCeiling);

                String idx = "w" + index;

                this.word = reader.getString(idx);
            } catch (Exception e) {
                this.word = "JUSTICE";
            }
        }

        private String loadJSONFromAsset(Context context, int difficulty) {
            String json = null;
            try {
                InputStream is;
                switch (difficulty) {
                    case 0:
                        //easy
                        is = context.getAssets().open("encrypted_easy.json");
                        break;
                    case 1:
                        // medium
                        is = context.getAssets().open("encrypted_medium.json");
                        break;
                    case 2:
                        // hard
                        is = context.getAssets().open("encrypted_hard.json");
                        break;
                    default:
                        is = context.getAssets().open("encrypted_medium.json");
                }
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;

        }
    }

    @Override
    public void callbackTimer() {
        if (time != null) {
            if (min_cur == 0) time.setTextColor(Color.RED);
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

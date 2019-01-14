package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
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


    //constructors
    public EncryptedActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }

    public EncryptedActivity() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //receive last game data
        receiveLastGameData();

        setupGame();
    }

    private void setupGame() {
        setContentView(R.layout.activity_encrypted);

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
        Word chosenWord = new Word();
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
                initializeNextGame();
            }
        } else {
            //if answer is false, turn button red
            button.setBackgroundColor(Color.RED);
            button.setEnabled(false);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 500ms
                    setupGame();
                }
            }, 500);
        }
    }


    private class Word {
        String word;

        public Word(){
            Random rand = new Random();
            String json = loadJSONFromAsset(EncryptedActivity.this);
            try {
                JSONObject reader = new JSONObject(json);
                int randCeiling = reader.getInt("nb");

                int index = rand.nextInt(randCeiling);

                String idx = "w" + index;

                this.word = reader.getString(idx);
            }
            catch (Exception e){
                this.word = "JUSTICE";
            }
        }

        private String loadJSONFromAsset(Context context) {
            String json = null;
            try {
                InputStream is = context.getAssets().open("encrypted.json");
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


}

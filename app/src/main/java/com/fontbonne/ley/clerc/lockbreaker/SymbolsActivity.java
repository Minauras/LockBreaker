package com.fontbonne.ley.clerc.lockbreaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SymbolsActivity extends MiniGame {

    private ArrayList<Character> symbols = new ArrayList<Character>();
    private ArrayList<Integer> solution = new ArrayList<Integer>();
    private ArrayList<Character> solution_symbol = new ArrayList<Character>();

    //constructors
    public SymbolsActivity(List<Class> gameActivity, int totscore, int difficulty) {
        super(gameActivity, totscore, difficulty);
    }

    public SymbolsActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //receive last game data
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

        setupGame();
    }

    private void setupGame() {
        setContentView(R.layout.activity_symbols);

        symbols.clear();
        solution.clear();
        solution_symbol.clear();

        //give each button a random, non-repeated symbol, out of the symbol list
        chooseSymbols();
        initButtons();

        //out of the selected symbols, choose four of them which will be the correct ones
        createSolution();

        //transmit the four symbols to the watch, and start its activity
        transmitSolution();
    }

    private void initButtons() {
        for(Integer i = 1; i <= 16; i++){
            String buttonID = "button" + i.toString();
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = findViewById(resID);
            String value = String.valueOf(symbols.get(i-1));
            button.setText(value);
        }
    }

    private void transmitSolution() {
        //cast solution to string for transmission
        StringBuilder result = new StringBuilder(solution_symbol.size());
        for (Character c : solution_symbol) {
            result.append(c);
        }
        String solution_symbol_string = result.toString();

        //transmit and start activity
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.SYMBOLS.name());
        intent.putExtra(WearService.DATAMAP_SYMBOL_ARRAYLIST, solution_symbol_string);
        startService(intent);
    }

    private void createSolution() {
        int symbolCounter = 0;

        while (symbolCounter != 4) {
            Random r = new Random();
            int index = r.nextInt(16);

            if (!solution.contains(index)) { //choose four ints/symbols which will be the solution
                solution.add(index);
                solution_symbol.add(symbols.get(index));
                symbolCounter = symbolCounter + 1;
            }
        }
    }

    private void chooseSymbols() {
        int symbolCounter = 0;
        ArrayList<Integer> chosenIndices = new ArrayList<Integer>();

        while (symbolCounter != 16) {
            Random r = new Random();
            int index = r.nextInt(23);

            if (!chosenIndices.contains(index)) { //add a random symbol to list and keep track of
                //what has been added to not add the same twice
                chosenIndices.add(index);
                symbols.add(getResources().getString(R.string.symbols).charAt(index));
                symbolCounter = symbolCounter + 1;
            }
        }
    }

    public void check_symbol(View view) {
        Button button = findViewById(view.getId());

        if(solution_symbol.contains(button.getText().charAt(0))){ //charAt(0) is a safety measure

            //if answer is correct, remove answer from solution
            int index = solution_symbol.indexOf(button.getText().charAt(0));
            solution_symbol.remove(index);

            //turn button green
            button.setBackgroundColor(Color.GREEN);

            if(solution_symbol.isEmpty()){
                //game is won
                initializeNextGame();
                finish();
            }
        }
        else{
            //if answer is false, turn button red
            button.setBackgroundColor(Color.RED);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    setupGame();
                }
            }, 500);
        }

        button.setEnabled(false);
    }
}

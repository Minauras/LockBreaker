package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.support.wearable.activity.WearableActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EncryptedActivity extends WearableActivity {

    public static final String ENCRYPTED_DATA = "ENCRYPTED_DATA";

    TextView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted);

        tableView = findViewById(R.id.tableView);
        tableView.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String data = intent.getStringExtra(ENCRYPTED_DATA);

        setupLabels(data);
    }

    private void setupLabels(String data) {
        ArrayList<String> parsed_data = new ArrayList<String>(Arrays.asList(data.split("%")));

        ArrayList<CharPair> pairs = new ArrayList<>();

        for(int i = 0; i < 16; i++){
            CharPair pair = new CharPair(parsed_data.get(i), parsed_data.get(i+16));
            pairs.add(pair);
        }

        Collections.shuffle(pairs);

        for(CharPair pair : pairs){
            tableView.setText(tableView.getText() + "\n" +
                                pair.letter + getString(R.string.tab) + getString(R.string.tab) + pair.symbol);
        }
    }

    private class CharPair{
        String symbol;
        String letter;

        public CharPair(String symbol, String letter){
            this.symbol = symbol;
            this.letter = letter;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

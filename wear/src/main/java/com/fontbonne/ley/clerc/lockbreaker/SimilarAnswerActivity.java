package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

public class SimilarAnswerActivity extends WearableActivity {

    public static final String ANSWER = "ANSWER";

    private TextView mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("NICOLAS","SimilarAnswerActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_answer);
        Log.d("NICOLAS","SimilarAnswerActivity2");

        mAnswer = findViewById(R.id.answerTextView);
        Intent intent = getIntent();
        String name = intent.getStringExtra(ANSWER);
        mAnswer.setText(name);

    }
}

package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SimilarQuizActivity extends MiniGame {

    public SimilarQuizActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }

    public SimilarQuizActivity(){
        super();
    }


    private final int NB_QUESTIONS = 3;
    private Question[] questions;
    private List<Integer> questionID;
    private int currentQuestion;
    private int score = 0;

    private TextView questionTextView;
    private Button answer0;
    private Button answer1;
    private Button answer2;
    private Button answer3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_quiz);
        receiveLastGameData();

        Log.e("WALDO", "onCreate");

        questionTextView  = findViewById(R.id.questionTextView);
        answer0 = findViewById(R.id.answer0);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);


        Random rand = new Random();

        questions = new Question[NB_QUESTIONS];


        String json =  loadJSONFromAsset(this);

        Log.e("WALDO", "before JSON");

        try {
            JSONObject obj = new JSONObject(json);
            int numbersNeeded = obj.length();


            Set<Integer> generated = new LinkedHashSet<Integer>();
            while (generated.size() < NB_QUESTIONS)
            {
                Integer next = rand.nextInt(numbersNeeded) + 1;
                // As we're adding to a set, this will automatically do a containment check
                generated.add(next);
            }
            questionID = new ArrayList<>();
            questionID.addAll(generated);
            Log.d("AZERTY", questionID.toString());

            for (int i = 0; i < NB_QUESTIONS; i++){
                questions[i] = new Question(questionID.get(i), obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("WALDO", "after JSON");


        for (int i = 0; i < NB_QUESTIONS; i++){

            Log.d("AZERTY", questions[i].getQuestion());
        }

        currentQuestion = 0;
        updateLayout(currentQuestion);
    }

    public void updateLayout(int questionId){

        if (questionId < NB_QUESTIONS){

            Log.d("AZERTY", questions[questionId].getAnswer(questions[currentQuestion].getThruth()));
            startWatchActivity(questions[questionId].getAnswer(questions[currentQuestion].getThruth()));
            Log.d("AZERTY", "startWatchActivity");

            questionTextView.setText(questions[questionId].getQuestion());
            Log.d("AZERTY", "questionTextView");

            answer0.setText(questions[questionId].getAnswer(0));
            answer1.setText(questions[questionId].getAnswer(1));
            answer2.setText(questions[questionId].getAnswer(2));
            answer3.setText(questions[questionId].getAnswer(3));
            Log.d("AZERTY", "answer3");

        }
        else{
            initializeNextGame();
        }

    }

    public void CheckAnswer(View view) {

        int solFound = 0;
        int id = view.getId();
        switch (id){
            case R.id.answer0:
                solFound = 0;
                break;
            case R.id.answer1:
                solFound = 1;
                break;
            case R.id.answer2:
                solFound = 2;
                break;
            case R.id.answer3:
                solFound = 3;
                break;
        }
        if (solFound == questions[currentQuestion].getThruth()){
            Toast.makeText(this,"NICE", Toast.LENGTH_LONG).show();

            currentQuestion += 1;
            updateLayout(currentQuestion);
        }else{
            Toast.makeText(this,"WRONG !", Toast.LENGTH_LONG).show();
        }



    }



    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("quizz.json");
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

    private void startWatchActivity(String answer) {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.SIMILAR.name());
        intent.putExtra(WearService.SIMILAR, answer);
        startService(intent);
    }


}

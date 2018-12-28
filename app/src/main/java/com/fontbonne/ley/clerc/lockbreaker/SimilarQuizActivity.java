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

import java.util.List;

public class SimilarQuizActivity extends MiniGame {

    public SimilarQuizActivity(List<Class> gameActivity) {
        super(gameActivity);
        if(games.contains(SimilarQuizActivity.class)){
            int ind = games.indexOf(SimilarQuizActivity.class);
            nextGame = games.get(ind + 1);
        }else {
            Log.e("TAG_Patrick", "ERROR: not in games list");
        }
    }

    public SimilarQuizActivity(){
        super();
    }

    @Override
    protected void startGame(Context context){
        Intent intent = new Intent(context, SimilarQuizActivity.class);
        Bundle bundle = new Bundle();
        Log.e("TAG_Patrick", String.valueOf(this));
        bundle.putParcelable("data", this);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void findNext(){
        if(games.contains(SimilarQuizActivity.class)){
            int ind = games.indexOf(SimilarQuizActivity.class);
            nextGame = games.get(ind + 1);
        }else {
            Log.e("TAG_Patrick", "ERROR: not in games list");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScore(10);
                updateScore();
            }
        });
    }


}

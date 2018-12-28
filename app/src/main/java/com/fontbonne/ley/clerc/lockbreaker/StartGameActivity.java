package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StartGameActivity extends AppCompatActivity{

    private Button mStartButton;
    private Button mOptionButton;
    private Button mStatButton;

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

                Log.e("TAG_Patrick", "Bf games declaration");
                List<Class> games = new ArrayList<Class>();
                games.add(SimilarQuizActivity.class);
                games.add(MisleadingColorsActivity.class);
                games.add(FinalScreenActivity.class);

                Constructor<?> thingyconstructor = null;
                Object thingyobj = null;
                Method thingymethod = null;
                try {
                    thingyconstructor = games.get(0).getConstructor();
                    thingyobj = thingyconstructor.newInstance();
                    thingymethod = thingyobj.getClass().getDeclaredMethod("startGame", Context.class);
                    thingymethod.invoke(thingyobj, StartGameActivity.this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                Log.e("TAG_Patrick", "Finished Misleading Declaration");
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

    }
}

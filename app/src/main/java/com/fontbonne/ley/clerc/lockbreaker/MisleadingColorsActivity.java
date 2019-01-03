package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MisleadingColorsActivity extends MiniGame {

    public enum COLOR{
        RED, BLUE, GREEN, YELLOW
    }
    private static final Random RANDOM = new Random();
    private static final List<COLOR> VALUES =
            Collections.unmodifiableList(Arrays.asList(COLOR.values()));

    public MisleadingColorsActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }
    public MisleadingColorsActivity() {
        super();
    }

    Button btnTL;
    Button btnTR;
    Button btnBL;
    Button btnBR;

    TextView qstLeft;
    TextView qstRight;

    private COLOR[] colorOFtext;
    private COLOR[] colorOfcolor;
    private COLOR[] colorOFbtn;

    int answer;
    COLOR answerColor;

    int nbrGamesPlayed;

    private boolean gamemode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_misleading_colors);

        receiveLastGameData();

        nbrGamesPlayed = 0;

        Log.e("TAG_PAT", String.valueOf(VALUES));

        btnTL = findViewById(R.id.btnTopLeft);
        btnTR = findViewById(R.id.btnTopRight);
        btnBL = findViewById(R.id.btnBotLeft);
        btnBR = findViewById(R.id.btnBotRight);
        qstLeft = findViewById(R.id.QuestionTextView);
        qstRight = findViewById(R.id.QuestionColorView);

        btnTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer == 0) {
                    Log.e("TAG_RESULTS", "THIS IS CORRECT!!");
                    addtoScore(100);
                }
                else
                    Log.e("TAG_RESULTS", "THIS IS WRONG! Answer is " + answer);
                nbrGamesPlayed++;
                if(nbrGamesPlayed<3)setupGame();
                else initializeNextGame();
            }
        });

        btnTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer == 1) {
                    Log.e("TAG_RESULTS", "THIS IS CORRECT!!");
                    addtoScore(100);
                }
                else
                    Log.e("TAG_RESULTS", "THIS IS WRONG! Answer is " + answer);
                nbrGamesPlayed++;
                if(nbrGamesPlayed<3)setupGame();
                else initializeNextGame();
            }
        });

        btnBL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer == 2){
                    Log.e("TAG_RESULTS", "THIS IS CORRECT!!");
                    addtoScore(100);
                }
                else
                    Log.e("TAG_RESULTS", "THIS IS WRONG! Answer is " + answer);
                nbrGamesPlayed++;
                if(nbrGamesPlayed<3)setupGame();
                else initializeNextGame();
            }
        });

        btnBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer == 3) {
                    Log.e("TAG_RESULTS", "THIS IS CORRECT!!");
                    addtoScore(100);
                }
                else
                    Log.e("TAG_RESULTS", "THIS IS WRONG! Answer is " + answer);
                nbrGamesPlayed++;
                if(nbrGamesPlayed<3)setupGame();
                else initializeNextGame();
            }
        });
        setupGame();
    }

    private void setupGame(){
        initButtons();
        randColor();

        gamemode = RANDOM.nextBoolean();
        int rndInt = RANDOM.nextInt(4);
        COLOR color = VALUES.get(rndInt);
        if(gamemode) qstLeft.setText("Which color is ");
        else qstLeft.setText("Which word is ");
        findAnswer(color);
        setColorTxt(qstRight, color);

        sendInitParams();
    }

    private void findAnswer(COLOR color){
        int index = -1;
        if(gamemode){
            index = Arrays.asList(colorOFtext).indexOf(color);
            answerColor = Arrays.asList(colorOfcolor).get(index);
        }else{
            index = Arrays.asList(colorOfcolor).indexOf(color);
            answerColor = Arrays.asList(colorOFtext).get(index);
        }
        answer = Arrays.asList(colorOFbtn).indexOf(answerColor);
    }

    public void sendInitParams() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i = 0; i<4; i++){
            arrayList.add(convertto(colorOfcolor[i]));
            arrayList.add(convertto(colorOFtext[i]));
        }
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.MISLEADINGCOLORS.name());
        intent.putExtra(WearService.DATAMAP_COLOR_ARRAYLIST, arrayList);
        startService(intent);
    }

    private int convertto(COLOR color){
        switch (color){
            case RED:
                return 0;
            case BLUE:
                return 1;
            case GREEN:
                return 2;
            case YELLOW:
                return 3;
            default:
                Log.e("TAG_PAT", "ERROR THIS COLOR NOT KNOWN");
        }
        return -1;
    }

    private void initButtons() {
        int rndInt = RANDOM.nextInt(4);
        setColorBtn(btnTL, VALUES.get(rndInt));
        setColorBtn(btnTR, VALUES.get((rndInt+1)%4));
        setColorBtn(btnBL, VALUES.get((rndInt+2)%4));
        setColorBtn(btnBR, VALUES.get((rndInt+3)%4));
        colorOFbtn = new COLOR[]{VALUES.get(rndInt), VALUES.get((rndInt+1)%4),
                                 VALUES.get((rndInt+2)%4), VALUES.get((rndInt+3)%4)};
    }

    private void randColor(){
        int rndInt = RANDOM.nextInt(4);
        COLOR startingColor = VALUES.get(rndInt);
        COLOR scndColor = VALUES.get((rndInt+1)%4);
        COLOR thrdColor = VALUES.get((rndInt+2)%4);
        COLOR frthColor = VALUES.get((rndInt+3)%4);
        colorOFtext = new COLOR[]{startingColor, scndColor, thrdColor, frthColor};
        colorOfcolor = new COLOR[]{thrdColor, startingColor, frthColor, scndColor};
    }

    private void setColorBtn(Button btn, COLOR color){
        switch (color){
            case RED:
                btn.setBackgroundColor(Color.RED);
                break;
            case BLUE:
                btn.setBackgroundColor(Color.BLUE);
                break;
            case GREEN:
                btn.setBackgroundColor(Color.GREEN);
                break;
            case YELLOW:
                btn.setBackgroundColor(Color.YELLOW);
                break;
            default:
                Log.e("TAG_PAT", "ERROR THIS COLOR NOT KNOWN");
        }
    }

    private void setColorTxt(TextView text, COLOR color){
        switch (color){
            case RED:
                text.setText("Red");
                break;
            case BLUE:
                text.setText("Blue");
                break;
            case GREEN:
                text.setText("Green");
                break;
            case YELLOW:
                text.setText("Yellow");
                break;
            default:
                Log.e("TAG_PAT", "ERROR THIS COLOR NOT KNOWN");
        }
    }
}
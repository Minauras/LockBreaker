package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class WaldoActivity extends MiniGame //implements View.OnTouchListener
{

    private CharacterView mCharacter;
    private ViewGroup background;
    String waldosName = "";

    public WaldoActivity(List<Class> gameActivity, int totscore) {
        super(gameActivity, totscore);
    }

    public WaldoActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waldo);
        receiveLastGameData();

        startWatchActivity();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float width = (float)size.x;
        float height = (float)size.y;

        int nbx = 5;
        int nby = 5;

        // at scale 1 ~ (150, 300) => 2 ~ (300, 600)

        float dx = (width-110)/nbx;
        float dy = (height-610)/nby;

        Random rnd = new Random();

        int waldoIdx = rnd.nextInt(nbx*nby);

        background = findViewById(R.id.background);

        Date now = new Date();
        float x = 10, y = 10;


        for (int i = 0; i < nbx; i++){
            y = 10;
            for (int j = 0 ; j < nby; j++){
                String name =  now.toString() + ", " + String.valueOf(i)+ ", " + String.valueOf(j);
                if (i*nbx+j == waldoIdx){
                    waldosName = name;
                    mCharacter = new CharacterView(this, x, y,2L, name, true);
                }else{
                    mCharacter = new CharacterView(this, x, y,2L, name, false);
                }
                //mCharacter.setOnTouchListener(this);
                background.addView(mCharacter);
                y += dy;
            }
            x += dx;
        }
        Log.d("CHARLIE", waldosName);
        startWatchActivity();
    }
    public void win(){
        //Toast.makeText(this, "WIN", Toast.LENGTH_SHORT).show();
        initializeNextGame();
    }
/*
    public boolean onTouch(View v, MotionEvent event){

        CharacterView c = (CharacterView)v;
        float x  = event.getRawX();
        float y  = event.getRawY();
        float[] head = c.getTouchSurface();
        int action = event.getActionMasked();
        Log.d("AZERTY", String.valueOf(action));
        if (action == 0 && x > head[0] && x < head[1] && y > head[2] && y < head[3]){
            if (c.isWaldo()){
                //thruthClick = true;
                Toast.makeText(this, "WIN", Toast.LENGTH_SHORT).show();
            }else{
                //thruthClick = false;
                Toast.makeText(this, "LOSE", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
    */

    private void startWatchActivity() {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.WALDO.name());
        intent.putExtra(WearService.WALDO, waldosName);
        startService(intent);
    }
}

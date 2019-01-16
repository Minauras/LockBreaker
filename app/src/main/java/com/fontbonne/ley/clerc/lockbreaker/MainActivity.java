package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static android.util.Pair.*;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mScreen;
    private ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogo = (ImageView) findViewById(R.id.logoView);
        Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicStartScreenService.class);
        startService(intentmusic);
        mScreen = (RelativeLayout) findViewById(R.id.screen);
        mScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    //Pair<View,String> logoPair = new Pair<>(mLogo, "logoTransition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                            new Pair[]{create(mLogo, "logoTransition"),  create(mScreen, "screenTransition")});

                    //sendStart(v);
                    startActivity(toLogin, options.toBundle());
                } else {
                    startActivity(toLogin);
                }

            }
        });

    }

    // Start the WearOS ----------------------------------------------------------------------------

    public void sendStart(View view) {
        Intent intent = new Intent(this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.STARTACTIVITY.name());
        intent.putExtra(WearService.ACTIVITY_TO_START, BuildConfig.W_mainactivity);
        startService(intent);
    }
}

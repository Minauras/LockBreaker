package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

public class OptionActivity extends AppCompatActivity {
    public static final String DIFFICULTY_TAG = "DIFFICULTY";
    public static final String NUMBER_TAG = "NUMBER";


    RadioGroup mRadioGroup;

    int difficultyLevel;
    int nbrGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Intent intent = getIntent();

        difficultyLevel = intent.getIntExtra(OptionActivity.DIFFICULTY_TAG,1);
        nbrGames = intent.getIntExtra(OptionActivity.NUMBER_TAG,1);

        mRadioGroup = findViewById(R.id.radioGroup);
        //android:checkedButton="@+id/mediumRadio"
        switch (difficultyLevel){
            case 0:
                mRadioGroup.check(R.id.easyRadio);
                break;
            case 1:
                mRadioGroup.check(R.id.mediumRadio);
                break;
            case 2:
                mRadioGroup.check(R.id.hardRadio);
                break;
        }

        NumberPicker np = findViewById(R.id.numberPicker);

        np.setMinValue(1);
        Log.d("DEBUGNICO0", String.valueOf(nbrGames));
        np.setMaxValue(StartGameActivity.NBRMINIGAMES);
        np.setValue(nbrGames);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                nbrGames = newVal;
            }
        });

    }

    void onDifficultyChanged(View v){
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.easyRadio:
                if (checked)
                    difficultyLevel = 0;
                break;
            case R.id.mediumRadio:
                if (checked)
                    difficultyLevel = 1;
                break;
            case R.id.hardRadio:
                if (checked)
                    difficultyLevel = 2;
                break;
        }

    }

    public void validateOption(View view) {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra(OptionActivity.DIFFICULTY_TAG, difficultyLevel);
        intent.putExtra(OptionActivity.NUMBER_TAG, nbrGames);
        setResult(AppCompatActivity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity

            Log.e("TAG_PAT", "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY");
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicStartScreenService.class);
                stopService(intentmusic);
                Log.e("TAG_PAT", "YOU LEFT YOUR APP");
            }
            else {
                Log.e("TAG_PAT", "YOU SWITCHED ACTIVITIES WITHIN YOUR APP");
            }
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity

            Log.e("TAG_PAT", "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY");
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Log.e("TAG_PAT", "YOU LEFT YOUR APP");
            }
            else {
                Intent intentmusic = new Intent(getApplicationContext(), BackgroundMusicStartScreenService.class);
                startService(intentmusic);
                Log.e("TAG_PAT", "YOU SWITCHED ACTIVITIES WITHIN YOUR APP");
            }
        }
        super.onResume();
    }

}

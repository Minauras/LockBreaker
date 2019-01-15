package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionActivity extends AppCompatActivity {
    public static final String DIFFICULTY_TAG = "DIFFICULTY";


    RadioGroup mRadioGroup;

    int difficultyLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Intent intent = getIntent();

        difficultyLevel = intent.getIntExtra(OptionActivity.DIFFICULTY_TAG,1);

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
        setResult(AppCompatActivity.RESULT_OK, intent);
        Log.d("DEBUGNICO","HEY1");
        finish();
    }
}

package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class OptionActivity extends AppCompatActivity {

    int difficultyLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        difficultyLevel = 1;
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
    }
}

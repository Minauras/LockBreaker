package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {


    Button mRegisterButton;
    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);
        setContentView(R.layout.activity_login);
        mRegisterButton = (Button) findViewById(R.id.registerBtn);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
            }
        });

        mLoginButton =(Button) findViewById(R.id.loginBtn);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toStartGame = new Intent(LoginActivity.this, StartGameActivity.class);
                startActivity(toStartGame);
            }
        });
    }
}

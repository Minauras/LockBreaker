package com.fontbonne.ley.clerc.lockbreaker;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    // User ----------------------------------------------------------------------------------------
    private UserProfile mUserProfile;

    // Firebase ------------------------------------------------------------------------------------
    private FirebaseAuth mAuth;

    // Interface -----------------------------------------------------------------------------------
    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    /***********************************************************************************************
     *
     *  onCreate
     *
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase initialization -----------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();

        // Interface Linking -----------------------------------------------------------------------
        Button doneButton = (Button) findViewById(R.id.doneButton);
        mEmailEditText = (EditText) findViewById(R.id.emailText);
        mUsernameEditText = (EditText) findViewById(R.id.usernameText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordText);
/*
        CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
            }
        });
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
*/

        // Done Button Callback --------------------------------------------------------------------
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    /***********************************************************************************************
     *
     *  registerUser
     *  Check validity of email, username and password
     *  Then register the user with it email using Firebase Authentification
     *
     ***********************************************************************************************/

    private void registerUser() {

        // Get String from layout ------------------------------------------------------------------
        String email = mEmailEditText.getText().toString().trim();
        String username = mUsernameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        // Check Email -----------------------------------------------------------------------------
        if (email.isEmpty()){
            mEmailEditText.setError("Email is required");
            mEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailEditText.setError("Email is not valid");
            mEmailEditText.requestFocus();
            return;
        }

        // Check Username --------------------------------------------------------------------------
        if (username.isEmpty()){
            mUsernameEditText.setError("Username is required");
            mUsernameEditText.requestFocus();
            return;
        }

        // Check Password --------------------------------------------------------------------------
        if (password.isEmpty()){
            mPasswordEditText.setError("Password is required");
            mPasswordEditText.requestFocus();
            return;
        }
        if (password.length() < 6){
            mPasswordEditText.setError("Minimum length of password should be 6");
            mPasswordEditText.requestFocus();
            return;
        }

        // Create profile Locally ------------------------------------------------------------------
        mUserProfile = new UserProfile(email, username, password, "Suisse");

        // Register profile in Firebase ------------------------------------------------------------
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // If success, return to login screen ----------------------------------------------
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Successfully registered user", Toast.LENGTH_SHORT).show();
                    mUserProfile.uploadToFirebase();
                    Log.d("DEBUGFIRE3","HEY1");

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra(UserProfile.USER_PROFILE_TAG, mUserProfile);
                    setResult(AppCompatActivity.RESULT_OK, intent);
                    Log.d("DEBUGFIRE4","HEY1");
                    finish();


                    // Else check errors ---------------------------------------------------------------
                }else{
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed user registration", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


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

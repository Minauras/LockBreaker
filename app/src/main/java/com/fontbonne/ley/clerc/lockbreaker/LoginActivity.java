package com.fontbonne.ley.clerc.lockbreaker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    String CHANNEL_ID = "my_channel_01";// The id of the channel.

    // User ----------------------------------------------------------------------------------------
    private UserProfile mUserProfile = null;

    // Firebase ------------------------------------------------------------------------------------
    private FirebaseAuth mAuth;

    // Interface -----------------------------------------------------------------------------------
    private Button mRegisterButton;
    private Button mLoginButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    // TAGs ----------------------------------------------------------------------------------------
    private static final int REGISTER_PROFILE = 1;

    // Notifs ----------------------------------------------------------------------------------------
    public static int NOTIFICATION_ID = 0;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    /***********************************************************************************************
     *
     *  onCreate
     *
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);
        setContentView(R.layout.activity_login);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        CharSequence name = "Lock Breaker";// The user-visible name of the channel.

        createNotificationChannel();
        mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID )
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("Welcome")
                .setContentText("Welcome to our amazing game !");

        Notification myNotification = mBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID++, myNotification);
        Log.d("NICO", myNotification.toString());

        Log.d("NICO", mBuilder.toString());

        // Firebase initialization -----------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();

        // Interface initialization ----------------------------------------------------------------
        mEmailEditText = (EditText) findViewById(R.id.emailText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordText);
        mRegisterButton = (Button) findViewById(R.id.registerBtn);
        mLoginButton =(Button) findViewById(R.id.loginBtn);

        // TEMPORARY for quick pass through --------------------------------------------------------
        mEmailEditText.setText("test@test.com");
        mPasswordEditText.setText("password");
        // TEMPORARY -------------------------------------------------------------------------------

        // Register Button Callback ----------------------------------------------------------------
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                if (mUserProfile != null) {
                    toRegister.putExtra(UserProfile.USER_PROFILE_TAG, mUserProfile);
                }
                startActivityForResult(toRegister, REGISTER_PROFILE);
            }
        });

        // Login Button Callback -------------------------------------------------------------------
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogIn();
            }
        });
    }

    /***********************************************************************************************
     *
     *  userLogIn
     *  Check validity of email and password
     *  Then check the user with it email using Firebase Authentification
     *  And go to the StartGame Activity
     *
     ***********************************************************************************************/

    private void userLogIn(){

        // Get String from layout ------------------------------------------------------------------
        String email = mEmailEditText.getText().toString().trim();
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

        // Check Password --------------------------------------------------------------------------
        if (password.isEmpty()){
            mPasswordEditText.setError("Password is required");
            mPasswordEditText.requestFocus();
            return;
        }
        if (password.length() < 6){
            mPasswordEditText.setError("Password is not valid");
            mPasswordEditText.requestFocus();
            return;
        }

        // Check profile in Firebase ---------------------------------------------------------------
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // If success, go to StartGameActivity ---------------------------------------------
                if (task.isSuccessful()){

                    Intent toStartGame = new Intent(LoginActivity.this, StartGameActivity.class);
                    startActivity(toStartGame);
                } else {
                // Else print error ----------------------------------------------------------------
                    Toast.makeText(getApplicationContext(), "Failed user Log In", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /***********************************************************************************************
     *
     *  onActivityResult
     *  get result from registration if occured
     *
     ***********************************************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_PROFILE && resultCode == RESULT_OK && data != null) {

            mUserProfile = (UserProfile) data.getSerializableExtra(UserProfile.USER_PROFILE_TAG);
            if (mUserProfile != null) {
                mEmailEditText.setText(mUserProfile.email);
                mPasswordEditText.setText(mUserProfile.password);
            }
        }
    }
}

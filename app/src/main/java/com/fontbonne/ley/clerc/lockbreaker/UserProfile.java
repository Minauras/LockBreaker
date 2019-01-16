package com.fontbonne.ley.clerc.lockbreaker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class UserProfile implements Serializable {

    String email;
    String username;
    String uID;
    String password;
    Integer maxScore;
    Integer order;
    String country;




    public static final String USER_PROFILE_TAG = "USER_PROFILE";


    public UserProfile(){
        // Firebase initialization -----------------------------------------------------------------
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            this.uID = user.getUid();
            this.email = user.getEmail();
            this.username = user.getDisplayName();
        }
        this.maxScore = 0;
        this.order = 0;



    }

    public UserProfile(String email, String username, String password, String country) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.uID = "";
        this.maxScore = new Integer(0);
        this.order = new Integer(0);
        this.country = country;

    }

    public void uploadToFirebase(){
        FirebaseAuth mAuth;
        DatabaseReference mDatabase;
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        uID = user.getUid();
        if ( uID != null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(uID).child("displayName").setValue(username);
            mDatabase.child(uID).child("maxScore").setValue(maxScore);
            mDatabase.child(uID).child("order").setValue(order);
            mDatabase.child(uID).child("country").setValue(country);

        }
    }

    public boolean updateScore(final int score){

        final boolean[] out = {false};
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( uID != null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference finalMDatabase = mDatabase;
            mDatabase.child(uID).child("maxScore").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    maxScore = dataSnapshot.getValue(Integer.class);

                    if (score > maxScore.intValue()){
                        finalMDatabase.child(uID).child("maxScore").setValue(score);
                        finalMDatabase.child(uID).child("order").setValue(-score);
                        out[0] = true;
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



        return out[0];
    }



}

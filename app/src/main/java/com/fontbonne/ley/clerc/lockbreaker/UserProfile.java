package com.fontbonne.ley.clerc.lockbreaker;

import java.io.Serializable;

public class UserProfile implements Serializable {

    String email;
    String username;
    String password;
    int easyScore;
    int normalScore;
    int hardScore;

    public static final String USER_PROFILE_TAG = "USER_PROFILE";



    UserProfile(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.easyScore = 0;
        this.normalScore = 0;
        this.hardScore = 0;
    }

}

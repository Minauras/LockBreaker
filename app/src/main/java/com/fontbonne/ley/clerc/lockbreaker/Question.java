package com.fontbonne.ley.clerc.lockbreaker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class Question {

    private String question;
    private String[] answer;



    private int thruth;

    public Question(int id, JSONObject obj){

        Random rand = new Random();
        thruth = rand.nextInt(4);
        answer =  new String[4];
        String name = "q"+String.valueOf(id);
        try {
            JSONObject q = obj.getJSONObject(name);
            question = q.getString("question");
            JSONArray answersJSON= q.getJSONArray("answers");

            for (int i = 0; i < 4;i++){
                answer[i] = answersJSON.getString(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getQuestion() {
        return question;
    }

    public int getThruth() {
        return thruth;
    }

    public String getAnswer(int id) {
        return answer[id];
    }

    @Override
    public String toString() {
        return question + "\n" + "\t" + answer[0] + "\n" + "\t" + answer[1] + "\n" + "\t" + answer[2] + "\n" + "\t" + answer[3] + "\n" ;
    }
}

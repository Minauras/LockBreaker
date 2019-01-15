package com.fontbonne.ley.clerc.lockbreaker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<String> mArrayList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    private ListView mListView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArrayList);
        mListView = findViewById(R.id.dbList);
        mListView.setAdapter(mAdapter);

        Query myTopPScoreQuery = mDatabase.orderByChild("order");
        myTopPScoreQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String place = String.valueOf(i);
                    i++;
                    String name = postSnapshot.child("displayName").getValue(String.class);
                    String score = String.valueOf(postSnapshot.child("maxScore").getValue(Integer.class));

                    mArrayList.add(place + " : " + name + "           " + score + " points");
                    Log.d("NICO", name);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

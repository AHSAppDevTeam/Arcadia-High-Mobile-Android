package com.hsappdev.ahs.UI.lunchMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.db.DatabaseConstants;

public class LunchMenuActivity extends AppCompatActivity {

    private static final String TAG = "LunchMenuActivity";


    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_menu);

        TextView mondayLunchMenu = findViewById(R.id.lunchMenuMonday);
        TextView tuesdayLunchMenu = findViewById(R.id.lunchMenuTuesday);
        TextView wednesdayLunchMenu = findViewById(R.id.lunchMenuWednesday);
        TextView thursdayLunchMenu = findViewById(R.id.lunchMenuThursday);
        TextView fridayLunchMenu = findViewById(R.id.lunchMenuFriday);

        db = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB))
                .getReference();

        db.child("lunch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Lunch menu code1 " + dataSnapshot.getChildrenCount());


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Lunch menu code");
                    Log.d(TAG, postSnapshot.child("day").getValue(String.class));
                    Log.d(TAG, postSnapshot.child("menu").getValue(String.class));

                    String day = postSnapshot.child("day").getValue(String.class);

                    if (day.equals("Monday")) {
                        mondayLunchMenu.setText(postSnapshot.child("menu").getValue(String.class));
                    } else if (day.equals("Tuesday")) {
                        tuesdayLunchMenu.setText(postSnapshot.child("menu").getValue(String.class));
                    }  else if (day.equals("Wednesday")) {
                        wednesdayLunchMenu.setText(postSnapshot.child("menu").getValue(String.class));
                    }  else if (day.equals("Thursday")) {
                        thursdayLunchMenu.setText(postSnapshot.child("menu").getValue(String.class));
                    }  else if (day.equals("Friday")) {
                        fridayLunchMenu.setText(postSnapshot.child("menu").getValue(String.class));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


    }


}
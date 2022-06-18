package com.hsappdev.ahs.newDataTypes;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.AbstractMap;
import java.util.HashMap;

public class WeekData {
    private String title;
    private String weekId;

    /**
     * Day of the weeks
     * index 0 = nothing,
     * index 1 = monday,
     * index 2 = tuesday,
     * index 3 = wednesday
     * so on...
     */

    private HashMap<Integer, MutableLiveData<DayData>> dayList = new HashMap<>();

    public WeekData(String weekId) {
        this.weekId = weekId;
        load();
    }

    private void load() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child("weeks").child(weekId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    return;
                }
                title = snapshot.child("title").getValue(String.class);
                int dayNum = 0;
                for(DataSnapshot ds : snapshot.child("scheduleIDs").getChildren()) {
                    String scheduleId = ds.getValue(String.class);
                    MutableLiveData<DayData> dayLiveData = new MutableLiveData<>();
                    dayLiveData.setValue(new DayData(scheduleId));
                    dayList.put(dayNum, dayLiveData);
                    dayNum++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // GETTERS AND SETTERS


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public HashMap<Integer, MutableLiveData<DayData>> getDayList() {
        return dayList;
    }

}

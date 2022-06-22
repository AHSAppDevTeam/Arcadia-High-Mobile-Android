package com.hsappdev.ahs.newDataTypes;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
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

    public WeekData(String weekId, Resources r) {
        this.weekId = weekId;
        load(r);
    }

    private void load(Resources r) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_weeks_id)).child(weekId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    return;
                }
                title = snapshot.child(r.getString(R.string.db_weeks_title)).getValue(String.class);
                int dayNum = 0;
                for(DataSnapshot ds : snapshot.child(r.getString(R.string.db_weeks_schedule_ids)).getChildren()) {
                    String scheduleId = ds.getValue(String.class);
                    MutableLiveData<DayData> dayLiveData = new MutableLiveData<>();
                    dayLiveData.setValue(new DayData(scheduleId, r));
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

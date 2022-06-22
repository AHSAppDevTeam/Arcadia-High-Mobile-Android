package com.hsappdev.ahs.newDataTypes;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.db.DatabaseConstants;

public class DayData {

    private final String scheduleId;

    private ScheduleData scheduleCollector = null;

    private final MutableLiveData<ScheduleData> schedule = new MutableLiveData<>();

    public DayData(String scheduleId, Resources r){
        this.scheduleId = scheduleId;
        loadScheduleFromFirebase(r);
    }

    private void loadScheduleFromFirebase(Resources r) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_schedule_id))
                .child(scheduleId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null){ return; }
                scheduleCollector = new ScheduleData();
                scheduleCollector.setIconURL(snapshot.child(r.getString(R.string.db_schedule_icon)).getValue(String.class));
                scheduleCollector.setColor(snapshot.child(r.getString(R.string.db_schedule_color)).getValue(String.class));
                scheduleCollector.setTitle(snapshot.child(r.getString(R.string.db_schedule_title)).getValue(String.class));
                scheduleCollector.setDots(snapshot.hasChild(r.getString(R.string.db_schedule_dots)) ? snapshot.child("dots").getValue(int.class) : 0);

                scheduleCollector.getTimestamps().clear();
                for(DataSnapshot ds : snapshot.child(r.getString(R.string.db_schedule_timestamps)).getChildren()) {
                    Integer timestamp = ds.getValue(Integer.class);
                    scheduleCollector.getTimestamps().add(timestamp);
                }

                scheduleCollector.getPeriodIDs().clear();
                for(DataSnapshot ds : snapshot.child(r.getString(R.string.db_schedule_period_id)).getChildren()) {
                    String periodID = ds.getValue(String.class);
                    scheduleCollector.getPeriodIDs().add(periodID);
                }

                schedule.postValue(scheduleCollector);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // GETTERS AND SETTERS


    public String getScheduleId() {
        return scheduleId;
    }

    public ScheduleData getScheduleCollector() {
        return scheduleCollector;
    }

    public MutableLiveData<ScheduleData> getSchedule() {
        return schedule;
    }
}

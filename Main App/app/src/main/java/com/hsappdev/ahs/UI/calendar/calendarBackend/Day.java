package com.hsappdev.ahs.UI.calendar.calendarBackend;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

public class Day {

    private static final String TAG = "Day";

    private String scheduleId;
            // the schedule for the day
    private Schedule schedule = null;

    private List<CalendarScheduleLoadCallback> callbacks = new ArrayList<>();

    public Day(String scheduleId) {
        this.scheduleId = scheduleId;
        startLoad();
    }

    public void startLoad() {
        loadScheduleFromFirebase();
    }

    private void loadScheduleFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child("schedules")
                .child(scheduleId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null){ return; }
                schedule = new Schedule();
                schedule.setIconURL(snapshot.child("iconURL").getValue(String.class));
                schedule.setColor(snapshot.child("color").getValue(String.class));
                schedule.setTitle(snapshot.child("title").getValue(String.class));

                schedule.getTimestamps().clear();
                for(DataSnapshot ds : snapshot.child("timestamps").getChildren()) {
                    Integer timestamp = ds.getValue(Integer.class);
                    schedule.getTimestamps().add(timestamp);
                }

                schedule.getPeriodIDs().clear();
                for(DataSnapshot ds : snapshot.child("periodIDs").getChildren()) {
                    String periodID = ds.getValue(String.class);
                    schedule.getPeriodIDs().add(periodID);
                }

                for(CalendarScheduleLoadCallback callback : callbacks) {
                    callback.onCalendarScheduleLoad(schedule);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadSchedule(CalendarScheduleLoadCallback callback){
        if(schedule != null){
            callback.onCalendarScheduleLoad(schedule);
        } else {
            registerCallback(callback);
        }

    }

    private void registerCallback(CalendarScheduleLoadCallback callback) {
        callbacks.add(callback);
    }

    // GETTERS AND SETTERS
    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
}

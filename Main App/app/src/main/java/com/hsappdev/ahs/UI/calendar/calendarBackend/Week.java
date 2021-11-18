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
import java.util.HashMap;
import java.util.List;

public class Week {
    private String title;
    private String weekId;
                    // day of week (0=sunday, or 0=monday not sure?)
    private HashMap<Integer, Day> dayList = new HashMap<>();

    private List<CalendarDayLoadCallback> callbacks = new ArrayList<>();

    public Week() {
    }

    public Week(String weekId) {
        this.weekId = weekId;
    }

    public void load() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child("weeks").child(weekId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title = snapshot.child("title").getValue(String.class);
                int dayNum = 0;
                for(DataSnapshot ds : snapshot.child("scheduleIDs").getChildren()) {
                    String scheduleId = ds.getValue(String.class);
                    dayList.put(dayNum, new Day(scheduleId));
                    dayNum++;
                }
                for(CalendarDayLoadCallback callback : callbacks) {
                    callback.onCalendarDayLoad(dayList.get(callback.getRequestedDate()));
                }
                // clear list
                callbacks.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // GETTERS AND SETTERS
    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public HashMap<Integer, Day> getDayList() {
        return dayList;
    }

    public String getTitle() {
        return title;
    }

    public List<CalendarDayLoadCallback> getCallbacks() {
        return callbacks;
    }
}

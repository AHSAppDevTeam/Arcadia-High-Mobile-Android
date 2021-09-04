package com.hsappdev.ahs.UI.calendar.newCalendar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.UI.calendar.DayViewContainer;
import com.hsappdev.ahs.UI.calendar.MonthHeaderContainer;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarDayLoadCallback;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Day;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Week;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.util.HashMap;

public class CalendarBackendNew {

    private static final String TAG = "CalendarBackendNew";

    private static CalendarBackendNew calendarBackend;
    private final CalendarView calendarView;

    private HashMap<Integer, Week> weekIds = new HashMap<>();

    public static CalendarBackendNew getInstance(CalendarView calendarView) {
        calendarBackend = new CalendarBackendNew(calendarView); // new fresh instance
        return calendarBackend;
    }

    public static CalendarBackendNew getInstance() {
        return calendarBackend;
    }

    private CalendarBackendNew(CalendarView calendarView) {
        this.calendarView = calendarView;
        setUpCalendarLibrary();
        setUp();
    }

    public void setUp() {
        // populate with empty week objects
        // assume 54 weeks
        for (int i = 0; i <= 53; i++) { // start at 1
            weekIds.put(i, new Week());
        }
        loadWeekIds();
    }

    public void registerForCallback(int weekNumber, int dayNumber, CalendarDayLoadCallback dayViewContainer) {
        Day requestedDate = getDay(weekNumber, dayNumber);
        if (requestedDate == null) {
            // add callback to week
            weekIds.get(weekNumber).getCallbacks().add(dayViewContainer);
        } else {
            // return the data
            dayViewContainer.onCalendarDayLoad(requestedDate);
            Log.d(TAG, "registerForCallback: " + weekIds.get(weekNumber).getWeekId());
        }
    }

    private Day getDay(int weekNumber, int dayNumber) {
        return weekIds.get(weekNumber).getDayList().get(dayNumber);
    }

    private void loadWeekIds() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child("weekIDs");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int weekOfYear = 1;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String weekId = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "onDataChange: " + dataSnapshot.getValue(String.class));
                    Week week = weekIds.get(weekOfYear);
                    week.setWeekId(weekId);
                    week.load();
                    weekOfYear++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUpCalendarLibrary() {

        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderContainer>() {
            @NotNull
            @Override
            public MonthHeaderContainer create(@NotNull View view) {
                return new MonthHeaderContainer(view);
            }

            @Override
            public void bind(@NotNull MonthHeaderContainer viewContainer, @NotNull CalendarMonth calendarMonth) {
                viewContainer.updateView(calendarMonth);
            }
        });
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @NotNull
            @Override
            public DayViewContainer create(@NotNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NotNull DayViewContainer viewContainer, @NotNull CalendarDay calendarDay) {
                viewContainer.updateView(calendarDay);
            }
        });
        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(10);
        YearMonth lastMonth = currentMonth.plusMonths(10);
        calendarView.setup(firstMonth, lastMonth, DayOfWeek.SUNDAY);
        calendarView.scrollToMonth(currentMonth);
    }

}

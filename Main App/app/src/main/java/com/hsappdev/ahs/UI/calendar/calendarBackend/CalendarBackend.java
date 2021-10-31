package com.hsappdev.ahs.UI.calendar.calendarBackend;

import android.content.res.Resources;

import com.hsappdev.ahs.UI.calendar.newCalendar.ScheduleRenderer;
import com.kizitonwose.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;

public class CalendarBackend {

    private static final String TAG = "CalendarBackend";
    
    private CalendarView calendarView;
    private List<String> weekIds = new ArrayList<>();
    private final Resources r;

    private ScheduleRenderer scheduleRenderer;


    private static CalendarBackend calendarBackend;

    public static CalendarBackend getInstance(CalendarView calendarView, ScheduleRenderer scheduleRenderer) {
        if(calendarBackend == null) {
            calendarBackend = new CalendarBackend(calendarView, scheduleRenderer);
        }
        return calendarBackend;
    }

    public static CalendarBackend getInstance() {
        return calendarBackend;
    }

    private CalendarBackend(CalendarView calendarView, ScheduleRenderer scheduleRenderer) {
        this.calendarView = calendarView;
        this.scheduleRenderer = scheduleRenderer;
        r = calendarView.getResources();
    }

//    public void setUp() {
//        loadWeekIds();
//
//    }

//    private void continueAfterFirebaseLoad() {
//        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderContainer>() {
//            @NotNull
//            @Override
//            public MonthHeaderContainer create(@NotNull View view) {
//                return new MonthHeaderContainer(view, CalendarBackend.this);
//            }
//
//            @Override
//            public void bind(@NotNull MonthHeaderContainer viewContainer, @NotNull CalendarMonth calendarMonth) {
//                viewContainer.updateView(calendarMonth);
//            }
//        });
//        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
//            @NotNull
//            @Override
//            public DayViewContainer create(@NotNull View view) {
//                return new DayViewContainer(view, CalendarBackend.this);
//            }
//
//            @Override
//            public void bind(@NotNull DayViewContainer viewContainer, @NotNull CalendarDay calendarDay) {
//                viewContainer.updateView(calendarDay);
//            }
//        });
//        YearMonth currentMonth = YearMonth.now();
//        YearMonth firstMonth = currentMonth.minusMonths(10);
//        YearMonth lastMonth = currentMonth.plusMonths(10);
//        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
//        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
//        calendarView.scrollToMonth(currentMonth);
//    }

//    private void loadWeekIds() {
//        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
//                .child("weekIDs");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                weekIds.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String weekId = dataSnapshot.getValue(String.class);
//                    Log.d(TAG, "onDataChange: " + dataSnapshot.getValue(String.class));
//                    weekIds.add(weekId);
//                }
//                continueAfterFirebaseLoad();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    public void loadWeekData(String weekId, DayViewContainer callback) {
//        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
//                .child("weeks").child(weekId);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Week week = new Week();
//                week.setTitle(snapshot.child("title").getValue(String.class));
//                int dayNum = 0;
//                for(DataSnapshot ds : snapshot.child("scheduleIDs").getChildren()) {
//                    dayNum++; // start at 1
//                    Day day = new Day();
//                    day.setScheduleId(ds.getValue(String.class));
//                    week.getDayList().put(dayNum, day);
//                    Log.d(TAG, "onDataChange: "+ day.getScheduleId());
//                }
//                callback.onFirebaseLoad(week);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    // GETTERS AND SETTERS
    public List<String> getWeekIds() {
        return weekIds;
    }


}

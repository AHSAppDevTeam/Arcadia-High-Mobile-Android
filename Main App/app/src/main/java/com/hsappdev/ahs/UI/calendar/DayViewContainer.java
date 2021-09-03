package com.hsappdev.ahs.UI.calendar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarBackend;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarDayLoadCallback;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Day;
import com.hsappdev.ahs.UI.calendar.newCalendar.CalendarBackendNew;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DayViewContainer extends ViewContainer implements CalendarDayLoadCallback {

    private static final String TAG = "DayViewContainer";

    private TextView dayText;
    private LocalDate date;


    public DayViewContainer(@NotNull View view) {
        super(view);
        dayText = view.findViewById(R.id.calendarDayText);

    }

    public void updateView(CalendarDay calendarDay) {
        date = calendarDay.getDate();
        CalendarBackendNew.getInstance().registerForCallback(getWeekOfYear(), getDayOfWeek(), this);
    }


    public void onDataLoad(Day requestedDate) {
        dayText.setText(requestedDate.getScheduleId());
    }

    @Override
    public int getRequestedDate() {
        return getDayOfWeek();
    }


    // Helper
    private int getWeekOfYear() {
        WeekFields weekFields = WeekFields.of(Locale.US);
        Log.d(TAG, "getWeekOfYear: " + LocalDate.now().get(WeekFields.of(Locale.US).weekOfYear()));
        return date.get(weekFields.weekOfWeekBasedYear())-1;
    }

    private int getDayOfWeek() {
        return date.getDayOfWeek().getValue();
    }
}

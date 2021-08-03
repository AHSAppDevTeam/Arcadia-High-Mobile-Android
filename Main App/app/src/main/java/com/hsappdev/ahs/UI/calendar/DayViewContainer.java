package com.hsappdev.ahs.UI.calendar;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarBackend;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Day;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Week;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DayViewContainer extends ViewContainer {
    private TextView dayText;
    private LocalDate date;
    private CalendarDay calendarDay;

    private CalendarBackend calendarBackend;
    private boolean firebaseLoadFinished = false;

    public DayViewContainer(@NotNull View view, CalendarBackend calendarBackend) {
        super(view);
        this.calendarBackend = calendarBackend;
        dayText = view.findViewById(R.id.calendarDayText);

    }

    public void updateView(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
        date = calendarDay.getDate();
        setDateText(date.getDayOfMonth());
        setDateColor(calendarDay.getOwner() == DayOwner.THIS_MONTH, isDateToday());
    }

    private void setDateColor(boolean isThisMonth, boolean isDateToday) {
        if(isThisMonth) {
            dayText.setTextColor(Color.BLACK);
        } else {
            dayText.setTextColor(Color.GRAY);
        }
        if(isDateToday) {
            dayText.setBackgroundColor(Color.YELLOW);
        } else {
            dayText.setBackgroundColor(Color.LTGRAY);
        }
    }

    public void onFirebaseLoad(Week week) {
        firebaseLoadFinished = true;
        dayText.setText(week.getDayList().get((date.getDayOfWeek().getValue())%7+1).getScheduleId());
    }

    private boolean isDateToday() {
        return date.isEqual(LocalDate.now());
    }

    private void setDateText(int day) {
        dayText.setText(Integer.toString(day));
        // TODO: TESTING
        calendarBackend.loadWeekData(calendarBackend.getWeekIds().get(getWeekOfYear()), this);

    }
    private int getWeekOfYear() {
        WeekFields weekFields = WeekFields.of(Locale.US);
        return date.get(weekFields.weekOfWeekBasedYear());
    }

//    public void setDate(int date, boolean isDayInMonth, boolean isToday) {
//        dayText.setText(Integer.toString(date));
//        if(isDayInMonth) {
//            dayText.setTextColor(Color.BLACK);
//        } else {
//            dayText.setTextColor(Color.GRAY);
//        }
//        if(isToday) {
//            dayText.setBackgroundColor(Color.YELLOW);
//        } else {
//            dayText.setBackgroundColor(Color.LTGRAY);
//        }
//    }
}

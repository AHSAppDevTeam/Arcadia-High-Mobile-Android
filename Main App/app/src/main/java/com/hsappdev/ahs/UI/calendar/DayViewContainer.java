package com.hsappdev.ahs.UI.calendar;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarDayLoadCallback;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarScheduleLoadCallback;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Day;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Schedule;
import com.hsappdev.ahs.UI.calendar.newCalendar.CalendarBackendNew;
import com.hsappdev.ahs.UI.calendar.newCalendar.ScheduleRenderer;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;

public class DayViewContainer extends ViewContainer implements CalendarDayLoadCallback, CalendarScheduleLoadCallback, View.OnClickListener {

    private static final String TAG = "DayViewContainer";

    private TextView dayText;
    private TextView dayInfo;
    private LocalDate date;

    private Schedule schedule;
    private ScheduleRenderer scheduleRenderer;


    public DayViewContainer(@NotNull View view) {
        super(view);
        dayText = view.findViewById(R.id.calendarDayText);
        dayInfo = view.findViewById(R.id.calendarDayInfo);
        view.setOnClickListener(this);
    }

    public void updateView(CalendarDay calendarDay, ScheduleRenderer scheduleRenderer) {
        this.scheduleRenderer = scheduleRenderer;
        date = calendarDay.getDate();
        dayText.setText(Integer.toString(calendarDay.getDay()));
        if(calendarDay.getOwner() == DayOwner.THIS_MONTH){
            dayText.setTextColor(Color.BLACK);
            if(calendarDay.getDay() == getDayOfMonth()) {
               dayText.setBackgroundColor(Color.YELLOW);
            } else {
                dayText.setBackgroundColor(Color.LTGRAY);
            }
        } else {
            dayText.setBackgroundColor(Color.LTGRAY);
            dayText.setTextColor(Color.GRAY);
        }
        CalendarBackendNew.getInstance().registerForCallback(getWeekOfYear(), getDayOfWeek(), this);
    }


    public void onCalendarDayLoad(Day requestedDate) {
        //dayText.setText(requestedDate.getScheduleId());
        requestedDate.loadSchedule(this);
    }

    @Override
    public int getRequestedDate() {
        return getDayOfWeek();
    }


    // Helper
    private int getWeekOfYear() {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        Log.d(TAG, "getWeekOfYear: " + LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private int getDayOfWeek() {
        return date.getDayOfWeek().getValue();
    }

    private int getDayOfMonth() {
        return LocalDate.now().getDayOfMonth();
    }

    @Override
    public void onCalendarScheduleLoad(Schedule schedule) {
        this.schedule = schedule;
        dayInfo.setBackgroundColor(Color.parseColor(schedule.getColor()));
        dayInfo.setText(schedule.getTitle());
    }

    @Override
    public void onClick(View v) {
        if(schedule != null && scheduleRenderer != null) {
            scheduleRenderer.render(schedule);
        }
    }
}

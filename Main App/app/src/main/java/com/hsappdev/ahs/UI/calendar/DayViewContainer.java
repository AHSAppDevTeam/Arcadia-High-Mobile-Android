package com.hsappdev.ahs.UI.calendar;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;

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

public class DayViewContainer extends ViewContainer implements CalendarDayLoadCallback, CalendarScheduleLoadCallback, View.OnClickListener, ScheduleRenderer.RemoveSelectedHighlightCallback {

    private static final String TAG = "DayViewContainer";

    private final TextView dayText;
    private final TextView dayDots;
    private LocalDate date;

    private Schedule schedule;
    private ScheduleRenderer scheduleRenderer;
    private CalendarDay calendarDay;


    public DayViewContainer(@NotNull View view) {
        super(view);
        dayText = view.findViewById(R.id.calendarDayText);
        dayDots = view.findViewById(R.id.calendarDayDots);
        view.setOnClickListener(this);
    }

    public void updateView(CalendarDay calendarDay, ScheduleRenderer scheduleRenderer) {
        this.calendarDay = calendarDay;
        this.scheduleRenderer = scheduleRenderer;
        scheduleRenderer.registerForRemoveHighlightCallback(this);
        date = calendarDay.getDate();
        dayText.setText(Integer.toString(calendarDay.getDay()));

        if(isToday() && schedule != null && scheduleRenderer != null)
            scheduleRenderer.render(schedule);

        toggleHighlight(false);

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
        dayDots.setTextColor(schedule.getColorInt());
        dayDots.setText(schedule.getDotsString());

        toggleHighlight(false);
        if(isToday()) {
            if (scheduleRenderer != null)
                scheduleRenderer.render(schedule);
        }

    }

    @Override
    public void onClick(View view) {
        toggleHighlight(true);
        if(schedule != null && scheduleRenderer != null) {
            scheduleRenderer.render(schedule);
        }
    }

    @Override
    public void removeHighlight() {
        toggleHighlight(false);
    }

    public void toggleHighlight(Boolean highlight) {
        int highlightedBackgroundColor = schedule != null ? schedule.getColorInt() : Color.BLUE;
        int highlightedTextColor = Color.WHITE;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = dayText.getContext().getTheme();

        theme.resolveAttribute(R.attr.backgroundColor, typedValue, true);
        int defaultBackgroundColor = typedValue.data;

        theme.resolveAttribute(R.attr.titleColor, typedValue, true);
        int defaultTextColor = typedValue.data;

        // refactor later for dark theme
        //theme.resolveAttribute(R.attr.mutedTitleColor, typedValue, true);
        int defaultMutedTextColor = Color.GRAY;

        if(isToday() || highlight) {
            dayText.setBackgroundColor(highlightedBackgroundColor);
            dayDots.setBackgroundColor(highlightedBackgroundColor);
            dayText.setTextColor(highlightedTextColor);
            dayDots.setTextColor(highlightedTextColor);
        } else {
            dayText.setBackgroundColor(defaultBackgroundColor);
            dayDots.setBackgroundColor(defaultBackgroundColor);
            if(calendarDay.getOwner() == DayOwner.THIS_MONTH) {
                dayText.setTextColor(defaultTextColor);
            } else {
                dayText.setTextColor(defaultMutedTextColor);
            }
            dayDots.setTextColor(highlightedBackgroundColor);
        }
    }


    private boolean isToday() {
        if(calendarDay.getOwner() == DayOwner.THIS_MONTH) {
            if (calendarDay.getDay() == getDayOfMonth()) {
                if (calendarDay.getDate().atStartOfDay().equals(LocalDate.now().atStartOfDay())) {
                    return true;
                }
            }
        }
        return false;
    }
}

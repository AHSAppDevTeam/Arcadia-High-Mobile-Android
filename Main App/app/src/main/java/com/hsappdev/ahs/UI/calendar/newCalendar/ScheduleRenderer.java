package com.hsappdev.ahs.UI.calendar.newCalendar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hsappdev.ahs.UI.calendar.calendarBackend.Schedule;

import org.w3c.dom.Text;

public class ScheduleRenderer {
    private final LinearLayout view;
    public ScheduleRenderer(LinearLayout view) {
        this.view = view;
    }

    public void render (Schedule schedule) {
        view.removeAllViews();
        TextView title = new TextView(view.getContext());
        title.setText(schedule.getTitle());
        view.addView(title);
    }
}

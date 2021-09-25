package com.hsappdev.ahs.UI.calendar.newCalendar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Schedule;

import org.w3c.dom.Text;

public class ScheduleRenderer {

    private static final String TAG = "ScheduleRenderer";
    
    private final LinearLayout view;
    public ScheduleRenderer(LinearLayout view) {
        this.view = view;
    }

    public void render (Schedule schedule) {
        view.removeAllViews();
        view.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(view.getContext());
        title.setText(schedule.getTitle());
        title.setTextSize(30f);
        view.addView(title);
        Log.d(TAG, "render: "+ schedule.getTimestamps().size());
        for (int i = 0; i < schedule.getTimestamps().size(); i+=2) {
            int timestampStart = schedule.getTimestamps().get(i);
            int timestampEnd = schedule.getTimestamps().get(i+1);
            int periodNum = i / 2 + 1;
            view.addView(generateScheduleBubbleView(timestampStart, timestampEnd, periodNum, view));
        }
    }

    private String getDisplayTime(int timestampStart) {
        int hour = timestampStart/60;
        String AMvsPM = "AM";
        if(hour >= 12) {hour -= 12; AMvsPM = "PM";}
        return String.format("%d:%02d %s", hour, timestampStart%60, AMvsPM);
    }

    private View generateScheduleBubbleView(int timestampStart, int timestampEnd, int periodNum, View view){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        View bubble = layoutInflater.inflate(R.layout.schedule_period_bubble, null, false);
        TextView timestampText = bubble.findViewById(R.id.schedule_period_bubble_text);
        timestampText.setText(String.format("Period %d %s to %s", periodNum, getDisplayTime(timestampStart), getDisplayTime(timestampEnd)));
        return bubble;
    }
}

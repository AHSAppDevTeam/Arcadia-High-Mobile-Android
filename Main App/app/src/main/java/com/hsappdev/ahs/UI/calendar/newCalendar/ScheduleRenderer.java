package com.hsappdev.ahs.UI.calendar.newCalendar;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Schedule;

import org.w3c.dom.Text;

public class ScheduleRenderer {

    private static final String TAG = "ScheduleRenderer";
    private static final int CELL_SCALE_FACTOR = 4;

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
            int periodNum = Integer.parseInt(schedule.getPeriodIDs().get(i));
            view.addView(generateScheduleBubbleView(timestampStart, timestampEnd, periodNum, view, schedule.getColor()));
            if(i+1 < schedule.getPeriodIDs().size()) {
                int passingPeriodEnd = schedule.getTimestamps().get(i+2);
                view.addView(generateSchedulePassingPeriodBubbleView(timestampEnd, passingPeriodEnd, schedule.getPeriodIDs().get(i + 1), view));
            }

        }
    }

    private String getDisplayTime(int timestampStart) {
        int hour = timestampStart/60;
        String AMvsPM = "AM";
        if(hour > 12) {hour -= 12; AMvsPM = "PM";}
        return String.format("%d:%02d %s", hour, timestampStart%60, AMvsPM);
    }

    private View generateScheduleBubbleView(int timestampStart, int timestampEnd, int periodNum, View view, String color){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        View bubble = layoutInflater.inflate(R.layout.schedule_period_bubble, null, false);
        TextView timestampTextStart = bubble.findViewById(R.id.schedule_period_bubble_time_start);
        TextView timestampTextEnd = bubble.findViewById(R.id.schedule_period_bubble_time_end);
        TextView periodText = bubble.findViewById(R.id.schedule_period_bubble_title);
        CardView periodBubble = bubble.findViewById(R.id.schedule_period_bubble);

        periodBubble.setBackgroundColor(Color.parseColor(color));

        int timePassed = timestampEnd-timestampStart;
        int cellHeight = timePassed*CELL_SCALE_FACTOR;
        bubble.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight));

        timestampTextStart.setText(getDisplayTime(timestampStart));
        timestampTextEnd.setText(getDisplayTime(timestampEnd));
        periodText.setText(String.format("Period %d", periodNum));
        return bubble;
    }

    private View generateSchedulePassingPeriodBubbleView(int timestampStart, int timestampEnd, String text, View view){
        View passingPeriodSpace = LayoutInflater.from(view.getContext()).inflate(R.layout.schedule_period_bubble_passing_period, null, false);
        TextView passingPeriodText = passingPeriodSpace.findViewById(R.id.schedule_period_bubble_passing_period_text);
        passingPeriodText.setText(text);
        passingPeriodText.setGravity(Gravity.CENTER);
        int timePassed = timestampEnd-timestampStart;
        int cellHeight = timePassed*CELL_SCALE_FACTOR;
        passingPeriodSpace.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight));
        return passingPeriodSpace;
    }
}

package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.hsappdev.ahs.UI.calendar.newCalendar.CalendarBackendNew;
import com.hsappdev.ahs.UI.calendar.newCalendar.ScheduleRenderer;

public class CalendarActivity extends AppCompatActivity {

    CalendarBackendNew calendarBackend;
    private ScheduleRenderer scheduleRenderer;
    private ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        setUpCustomCalendar();
    }

    private void setUpCustomCalendar() {
        scheduleRenderer = new ScheduleRenderer(findViewById(R.id.calendar_schedule_view));
        calendarBackend = CalendarBackendNew.getInstance(findViewById(R.id.calendarView), scheduleRenderer);

        homeButton = findViewById(R.id.calendar_activity_home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}//
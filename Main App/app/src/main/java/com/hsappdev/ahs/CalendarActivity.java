package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hsappdev.ahs.UI.calendar.DayViewContainer;
import com.hsappdev.ahs.UI.calendar.MonthHeaderContainer;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarBackend;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    CalendarBackend calendarBackend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        setUpCustomCalendar();
    }

    private void setUpCustomCalendar() {
        calendarBackend = CalendarBackend.getInstance(findViewById(R.id.calendarView));
        calendarBackend.setUp();
    }
}//
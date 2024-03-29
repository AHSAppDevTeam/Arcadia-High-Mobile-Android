package com.hsappdev.ahs.UI.calendar;

import android.view.View;
import android.widget.TextView;

import com.hsappdev.ahs.R;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

import java.time.format.TextStyle;
import java.util.Locale;

public class MonthHeaderContainer extends ViewContainer {
    private TextView monthText;
    private TextView yearText;
    public MonthHeaderContainer(@NotNull View view) {
        super(view);
        monthText = view.findViewById(R.id.calendarMonthText);
        yearText = view.findViewById(R.id.calendarMonthYearText);
    }

    public void updateView(CalendarMonth calendarDay) {
        setMonthText(calendarDay.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.US));
        setYearText(calendarDay.getYear());
    }

    private void setYearText(int year) {
        yearText.setText(Integer.toString(year));
    }

    private void setMonthText(String displayName) {
        monthText.setText(displayName);
    }
}

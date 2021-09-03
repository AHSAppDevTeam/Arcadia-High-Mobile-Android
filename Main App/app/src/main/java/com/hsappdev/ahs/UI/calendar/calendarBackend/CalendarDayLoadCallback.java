package com.hsappdev.ahs.UI.calendar.calendarBackend;

public interface CalendarDayLoadCallback {
    void onDataLoad(Day requestedDay);

    int getRequestedDate();
}

package com.hsappdev.ahs.UI.calendar.calendarBackend;

public interface CalendarDayLoadCallback {
    void onCalendarDayLoad(Day requestedDay);

    int getRequestedDate();
}

package com.hsappdev.ahs.UI.calendar.calendarBackend;

public class Day {
    private String scheduleId;

    public Day(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    // GETTERS AND SETTERS

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
}

package com.hsappdev.ahs.UI.calendar.calendarBackend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Week {
    private String title;
    private HashMap<Integer, Day> dayList = new HashMap<>();

    // GETTERS AND SETTERS
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<Integer, Day> getDayList() {
        return dayList;
    }

    public void setDayList(HashMap<Integer, Day> dayList) {
        this.dayList = dayList;
    }
}

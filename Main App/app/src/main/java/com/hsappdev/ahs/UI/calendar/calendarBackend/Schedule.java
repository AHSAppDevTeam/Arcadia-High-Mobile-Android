package com.hsappdev.ahs.UI.calendar.calendarBackend;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private String iconURL;
    private String color;
    private String title;
    // List of period ids, timestamps
    private List<String> periodIDs = new ArrayList<>(); // "1", "passing", "2", "lunch"
    private List<Integer> timestamps = new ArrayList<>(); // minutes from midnight


    // GETTERS AND SETTERS

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPeriodIDs() {
        return periodIDs;
    }

    public void setPeriodIDs(List<String> periodIDs) {
        this.periodIDs = periodIDs;
    }

    public List<Integer> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Integer> timestamps) {
        this.timestamps = timestamps;
    }
}

package com.hsappdev.ahs.dataTypes;

public class Category {
    private String categoryID, title;
    int color;

    public Category(String categoryID, String title, int color) {
        this.categoryID = categoryID;
        this.title = title;
        this.color = color;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
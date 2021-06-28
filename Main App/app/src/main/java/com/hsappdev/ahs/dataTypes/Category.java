package com.hsappdev.ahs.dataTypes;

import java.util.List;

public class Category {
    private String categoryID, title, iconURL;
    int color;
    private List<String> articleIds;

    public Category(String categoryID, String title, int color, String iconURL) {
        this.categoryID = categoryID;
        this.title = title;
        this.color = color;
        this.iconURL = iconURL;
    }

    public Category(String categoryID, String title, int color, String iconURL, List<String> articleIds) {
        this.categoryID = categoryID;
        this.title = title;
        this.color = color;
        this.iconURL = iconURL;
        this.articleIds = articleIds;
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

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public List<String> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
    }
}

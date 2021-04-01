package com.hsappdev.ahs.dataTypes;

public class Article {
    private String author;
    private String body;
    private String date;
    private String title;


    public Article(String author, String date, String title, String body) {
        this.author = author;
        this.date = date;
        this.title = title;
        this.body = body;
    }


    // GETTERS AND SETTERS


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

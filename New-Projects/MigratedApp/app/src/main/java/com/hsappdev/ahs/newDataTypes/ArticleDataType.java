package com.hsappdev.ahs.newDataTypes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.newCache.DataType;

public class ArticleDataType extends DataType {

    private String articleID;

    private String author;

    private String title;

    private String body;

    private String categoryID;

    private String categoryDisplayName;

    private int categoryDisplayColor;

    private String[] imageURLs;

    private String[] videoURLs;

    long timestamp;

    @Override
    public void setDataToView(View view) {
        TextView titleTextView = view.findViewById(R.id.board_title_text);

        titleTextView.setText(title);
    }

    @Override
    public void handleOnClick(View view) {

    }

    // GETTERS AND SETTERS

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
        this.dataId = articleID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryDisplayName() {
        return categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this.categoryDisplayName = categoryDisplayName;
    }

    public int getCategoryDisplayColor() {
        return categoryDisplayColor;
    }

    public void setCategoryDisplayColor(int categoryDisplayColor) {
        this.categoryDisplayColor = categoryDisplayColor;
    }

    public String[] getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(String[] imageURLs) {
        this.imageURLs = imageURLs;
    }

    public String[] getVideoURLs() {
        return videoURLs;
    }

    public void setVideoURLs(String[] videoURLs) {
        this.videoURLs = videoURLs;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return  "articleID: \t" + articleID + "\n" +
                "author: \t" + title + "\n" +
                "title: \t" + title + "\n" +
                "body: \t" + body + "\n" +
                "category: \t" + categoryID + "\n" +
                "categoryDisplayName: \t" + categoryDisplayName + "\n" +
                "imageURLs: \t" + ((imageURLs.length > 0) ? imageURLs[0] : "N/A") + "\n" +
                "timestamp: \t" + timestamp + "\n" +
                "color: \t" + categoryDisplayColor + "\n";
    }
}

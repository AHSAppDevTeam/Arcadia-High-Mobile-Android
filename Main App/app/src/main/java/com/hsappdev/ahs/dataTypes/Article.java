package com.hsappdev.ahs.dataTypes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Article implements Parcelable {
    private String articleID, author, title, body, category, categoryDisplayName;
    private int categoryDisplayColor;
    private String[] imageURLs;
    boolean featured;
    long timestamp;


    public Article(String articleID, String author, String title, String body, String category, String[] imageURLs, boolean featured, long timestamp) {
        this.articleID = articleID;
        this.author = author;
        this.title = title;
        this.body = body;
        this.category = category;
        this.imageURLs = imageURLs;
        this.featured = featured;
        this.timestamp = timestamp;
    }

    protected Article(Parcel in) {
        articleID = in.readString();
        author = in.readString();
        title = in.readString();
        body = in.readString();
        category = in.readString();
        categoryDisplayName = in.readString();
        categoryDisplayColor = in.readInt();
        imageURLs = in.createStringArray();
        featured = in.readByte() != 0;
        timestamp = in.readLong();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(String[] imageURLs) {
        this.imageURLs = imageURLs;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getCategoryDisplayColor() {
        return categoryDisplayColor;
    }

    public void setCategoryDisplayColor(int categoryDisplayColor) {
        this.categoryDisplayColor = categoryDisplayColor;
    }

    public String getCategoryDisplayName() {
        return categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this.categoryDisplayName = categoryDisplayName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(articleID);
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(category);
        parcel.writeString(categoryDisplayName);
        parcel.writeInt(categoryDisplayColor);
        parcel.writeStringArray(imageURLs);
        parcel.writeByte((byte) (featured ? 1 : 0));
        parcel.writeLong(timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return  "articleID: \t" + articleID + "\n" +
                "author: \t" + title + "\n" +
                "title: \t" + title + "\n" +
                "body: \t" + body + "\n" +
                "category: \t" + category + "\n" +
                "categoryDisplayName: \t" + categoryDisplayName + "\n" +
                "imageURLs: \t" + ((imageURLs.length > 0) ? imageURLs[0] : "N/A") + "\n" +
                "featured: \t" + featured + "\n" +
                "timestamp: \t" + timestamp + "\n" +
                "color: \t" + categoryDisplayColor + "\n";
    }
}

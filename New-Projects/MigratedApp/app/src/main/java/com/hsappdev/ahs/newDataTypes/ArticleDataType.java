package com.hsappdev.ahs.newDataTypes;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.newCache.DataType;

import java.util.Arrays;

public class ArticleDataType extends DataType implements Parcelable {

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

    int isSaved;

    int isNotification;

    int isViewed;


    public ArticleDataType(){

    }

    public static final Creator<ArticleDataType> CREATOR = new Creator<ArticleDataType>() {
        @Override
        public ArticleDataType createFromParcel(Parcel in) {
            return new ArticleDataType(in);
        }

        @Override
        public ArticleDataType[] newArray(int size) {
            return new ArticleDataType[size];
        }
    };

    protected ArticleDataType(Parcel in) {
        articleID = in.readString();
        author = in.readString();
        title = in.readString();
        body = in.readString();
        categoryID = in.readString();
        categoryDisplayName = in.readString();
        categoryDisplayColor = in.readInt();
        imageURLs = in.createStringArray();
        videoURLs = in.createStringArray();
        timestamp = in.readLong();
        isSaved = in.readInt();
        isNotification = in.readInt();
        isViewed = in.readInt();
        in.readParcelable(ClassLoader.getSystemClassLoader());
    }

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

    public int getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(int isSaved) {
        this.isSaved = isSaved;
    }

    public int getIsNotification() {
        return isNotification;
    }

    public void setIsNotification(int isNotification) {
        this.isNotification = isNotification;
    }

    public int getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(int isViewed) {
        this.isViewed = isViewed;
    }

    @Override
    public String toString() {
        return "ArticleDataType{" +
                "articleID='" + articleID + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", categoryID='" + categoryID + '\'' +
                ", categoryDisplayName='" + categoryDisplayName + '\'' +
                ", categoryDisplayColor=" + categoryDisplayColor +
                ", imageURLs=" + Arrays.toString(imageURLs) +
                ", videoURLs=" + Arrays.toString(videoURLs) +
                ", timestamp=" + timestamp +
                ", isSaved=" + isSaved +
                ", isNotification=" + isNotification +
                ", isViewed=" + isViewed +
                '}';
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
        parcel.writeString(categoryID);
        parcel.writeString(categoryDisplayName);
        parcel.writeInt(categoryDisplayColor);
        parcel.writeStringArray(imageURLs);
        parcel.writeStringArray(videoURLs);
        parcel.writeLong(timestamp);
        parcel.writeInt(isSaved);
        parcel.writeInt(isNotification);
        parcel.writeInt(isViewed);
    }
}

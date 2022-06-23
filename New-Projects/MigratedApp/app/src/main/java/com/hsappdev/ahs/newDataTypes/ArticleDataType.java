package com.hsappdev.ahs.newDataTypes;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.db.ArticleDAO;
import com.hsappdev.ahs.newCache.DataType;

import java.util.Arrays;

@Entity(tableName = ArticleDAO.TABLE_NAME)
public class ArticleDataType extends DataType implements Parcelable {

    @ColumnInfo(name = "ID")
    private int tableID;
    public int getTableID() {
        return tableID;
    }
    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public static final String ID = "IDS";
    @PrimaryKey
    @ColumnInfo(name = ID)
    @NonNull
    private String articleID;

    public static final String AUTHOR = "AUTHOR";
    @ColumnInfo(name = AUTHOR)
    private String author;

    public static final String TITLE = "TITLE";
    @ColumnInfo(name = TITLE)
    private String title;

    public static final String BODY = "BODY";
    @ColumnInfo(name = BODY)
    private String body;

    public static final String CAT_ID = "CAT_ID";
    @ColumnInfo(name = CAT_ID)
    private String categoryID;

    public static final String CAT_DISP = "CAT_DISP";
    @ColumnInfo(name = CAT_DISP)
    private String categoryDisplayName;

    public static final String CAT_DISP_CLR = "CAT_DISP_CLR";
    @ColumnInfo(name = CAT_DISP_CLR)
    private int categoryDisplayColor;

    public static final String IMG_URLS = "IMG_URLS";
    @ColumnInfo(name = IMG_URLS)
    private String[] imageURLs;

    public static final String VID_URLS = "VID_URLS";
    @ColumnInfo(name = VID_URLS)
    private String[] videoURLs;

    public static final String TIME = "TIME";
    @ColumnInfo(name = TIME)
    long timestamp;

    public static final String IS_SAVED = "IS_SAVED";
    @ColumnInfo(name = IS_SAVED)
    int isSaved;

    public static final String IS_NOTIFICATION = "IS_NOTIFICATION";
    @ColumnInfo(name = IS_NOTIFICATION)
    int isNotification;

    public static final String IS_VIEWED = "IS_VIEWED";
    @ColumnInfo(name = IS_VIEWED)
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

package com.hsappdev.ahs.dataTypes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.hsappdev.ahs.cache.LoadableType;
import com.hsappdev.ahs.newCache.CacheType;

@Entity(tableName = ArticleDAO.TABLE_NAME)
public class Article extends CacheType implements Parcelable, LoadableType {
    public static final String TABLE_NAME = ArticleDAO.TABLE_NAME;
    public static final String OLD_TABLE_NAME = "saved_table";

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

    @Ignore
    boolean featured;

    public Article(){

    }

    public Article(String articleID, String author, String title, String body, String categoryID, String[] imageURLs, String[] videoURls, boolean featured, long timestamp) {
        this.articleID = articleID;
        this.author = author;
        this.title = title;
        this.body = body;
        this.categoryID = categoryID;
        this.imageURLs = imageURLs;
        this.videoURLs = videoURls;
        this.featured = featured;
        this.timestamp = timestamp;
    }

    public Article(String articleID, String author, String title, String body, String categoryID, String[] imageURLs, String[] videoURLs, long timestamp, String categoryDisplayName, int categoryDisplayColor) {
        this.articleID = articleID;
        this.author = author;
        this.title = title;
        this.body = body;
        this.categoryID = categoryID;
        this.categoryDisplayName = categoryDisplayName;
        this.categoryDisplayColor = categoryDisplayColor;
        this.imageURLs = imageURLs;
        this.videoURLs = videoURLs;
        this.timestamp = timestamp;
    }

    protected Article(Parcel in) {
        articleID = in.readString();
        author = in.readString();
        title = in.readString();
        body = in.readString();
        categoryID = in.readString();
        categoryDisplayName = in.readString();
        categoryDisplayColor = in.readInt();
        imageURLs = in.createStringArray();
        videoURLs = in.createStringArray();
        featured = in.readByte() != 0;
        timestamp = in.readLong();
        isSaved = in.readInt();
        isNotification = in.readInt();
        isViewed = in.readInt();
        in.readParcelable(ClassLoader.getSystemClassLoader());
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

    public int getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(int isViewed) {
        this.isViewed = isViewed;
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

    public String[] getVideoURLs() {
        return videoURLs;
    }

    public void setVideoURLs(String[] videoURLs) {
        this.videoURLs = videoURLs;
    }

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

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
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
        parcel.writeString(categoryID);
        parcel.writeString(categoryDisplayName);
        parcel.writeInt(categoryDisplayColor);
        parcel.writeStringArray(imageURLs);
        parcel.writeStringArray(videoURLs);
        parcel.writeByte((byte) (featured ? 1 : 0));
        parcel.writeLong(timestamp);
        parcel.writeInt(isSaved);
        parcel.writeInt(isNotification);
        parcel.writeInt(isViewed);
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
                "featured: \t" + featured + "\n" +
                "timestamp: \t" + timestamp + "\n" +
                "color: \t" + categoryDisplayColor + "\n";
    }

    @Override
    public LoadableType getInstance() {
        return new Article();
    }
}

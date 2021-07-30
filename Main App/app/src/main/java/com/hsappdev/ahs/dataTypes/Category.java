package com.hsappdev.ahs.dataTypes;

import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hsappdev.ahs.cache.LoadableType;

import java.util.List;

@Entity(tableName = CategoryDAO.TABLE_NAME)
public class Category implements LoadableType {
    public static final String TABLE_NAME = CategoryDAO.TABLE_NAME;


    public static final String ID = "IDS";
    @PrimaryKey
    @ColumnInfo(name = ID)
    @NonNull
    private String categoryID;
    public static final String TITLE = "TITLE";
    @ColumnInfo(name = TITLE)
    private String title;
    public static final String IMG_URL = "IMG_URL";
    @ColumnInfo(name = IMG_URL)
    private String iconURL;
    public static final String CLR = "CLR";
    @ColumnInfo(name = CLR)
    int color;
    public static final String ARTICLE_IDS = "ARTICLE_IDS";
    @ColumnInfo(name = ARTICLE_IDS)
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

    public Category() {

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

    @Override
    public LoadableType getInstance() {
        return new Category();
    }
}

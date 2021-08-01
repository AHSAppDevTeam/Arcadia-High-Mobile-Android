package com.hsappdev.ahs.dataTypes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hsappdev.ahs.cache.LoadableType;

import java.util.List;

@Entity(tableName = CategoryListDAO.TABLE_NAME)
public class CategoryList implements LoadableType {
    public static final String TABLE_NAME = CategoryListDAO.TABLE_NAME;

    public static final String ID = "IDS";
    @PrimaryKey
    @ColumnInfo(name = ID)
    @NonNull
    private String categorySection;

    public static final String CATEGORY_IDS = "CATEGORY_IDS";
    @ColumnInfo(name = CATEGORY_IDS)
    private List<String> categoryList;

    @NonNull
    public String getCategorySection() {
        return categorySection;
    }

    public void setCategorySection(@NonNull String categorySection) {
        this.categorySection = categorySection;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public LoadableType getInstance() {
        return new CategoryList();
    }
}

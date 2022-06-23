package com.hsappdev.ahs.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hsappdev.ahs.newDataTypes.ArticleDataType;

import java.util.List;

@Dao
public interface ArticleDAO {
    String TABLE_NAME = "article_table";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addOrUpdateArticles(ArticleDataType... articles);

    @Query("SELECT * FROM " + ArticleDAO.TABLE_NAME + " WHERE IDS = :ID")
    LiveData<ArticleDataType> getArticle(String ID);

    @Query("DELETE FROM " + ArticleDAO.TABLE_NAME + " WHERE " + ArticleDataType.ID + " = :ID")
    void deleteArticle(String ID);

    @Query("SELECT * FROM " + ArticleDAO.TABLE_NAME)
    LiveData<List<ArticleDataType>> getAllArticles();

    @Query("SELECT * FROM " + ArticleDAO.TABLE_NAME + " WHERE ( " + ArticleDataType.IS_SAVED + " = 1)")
    LiveData<List<ArticleDataType>> getAllSavedArticles();

    @Query("SELECT * FROM " + ArticleDAO.TABLE_NAME + " WHERE ( " + ArticleDataType.IS_NOTIFICATION + " = 1)")
    LiveData<List<ArticleDataType>> getAllNotificationArticles();

    @Query("DELETE FROM " + ArticleDAO.TABLE_NAME)
    void deleteAll();

}

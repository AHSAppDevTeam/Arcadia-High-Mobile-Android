package com.hsappdev.ahs.dataTypes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDAO {
    String TABLE_NAME = "new_saved_table";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Article... articles);

    @Query("SELECT * from " + Article.TABLE_NAME + " WHERE IDS= :ID")
    List<Article> getPossibleArticles(String ID);

    @Query("UPDATE " + Article.TABLE_NAME + " SET IS_SAVED = :newSavedValue WHERE IDS = :ID")
    void updateSaved(String ID, int newSavedValue);

    @Query("DELETE FROM " + Article.TABLE_NAME + " WHERE "+Article.ID+" = :ID")
    void delete(String ID);

    @Query("SELECT EXISTS(SELECT * FROM " + Article.TABLE_NAME + " WHERE (" + Article.ID + " = :ID AND " + Article.IS_SAVED + " = 1))")
    LiveData<Boolean> isArticleSaved(String ID);

    @Query("SELECT * FROM " + Article.TABLE_NAME + " WHERE ( " + Article.IS_SAVED + " = 1)")
    LiveData<List<Article>> getAllSavedArticles();

    @Query("DELETE FROM " + Article.TABLE_NAME)
    void deleteAll();


}

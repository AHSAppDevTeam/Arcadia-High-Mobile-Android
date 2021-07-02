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

    @Query("DELETE FROM " + Article.TABLE_NAME + " WHERE " + Article.ID + " = :ID")
    void delete(String ID);

    @Query("SELECT EXISTS(SELECT * FROM " + Article.TABLE_NAME + " WHERE " + Article.ID + " = :ID)")
    LiveData<Boolean> isArticleSaved(String ID);

    @Query("SELECT * FROM " + Article.TABLE_NAME)
    LiveData<List<Article>> getAllArticles();

    @Query("DELETE FROM " + Article.TABLE_NAME)
    void deleteAll();
}

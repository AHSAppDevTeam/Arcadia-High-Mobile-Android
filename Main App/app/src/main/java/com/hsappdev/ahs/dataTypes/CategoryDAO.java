package com.hsappdev.ahs.dataTypes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CategoryDAO {
    String TABLE_NAME = "new_category_table";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Category... articles);

    @Query("SELECT * FROM " + Category.TABLE_NAME + " WHERE " + Category.ID + " = :ID")
    LiveData<Category> getCategory(String ID);

    @Query("DELETE FROM " + Category.TABLE_NAME + " WHERE " + Category.ID + " = :ID")
    void delete(String ID);

}

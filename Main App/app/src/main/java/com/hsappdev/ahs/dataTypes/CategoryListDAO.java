package com.hsappdev.ahs.dataTypes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CategoryListDAO {
    String TABLE_NAME = "new_category_list_table";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(CategoryList... articles);

    @Query("SELECT * FROM " + CategoryList.TABLE_NAME + " WHERE " + CategoryList.ID + " = :ID")
    LiveData<CategoryList> getCategory(String ID);

    @Query("DELETE FROM " + CategoryList.TABLE_NAME + " WHERE " + CategoryList.ID + " = :ID")
    void delete(String ID);

}

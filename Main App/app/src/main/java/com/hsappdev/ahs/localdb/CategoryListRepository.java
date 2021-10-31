package com.hsappdev.ahs.localdb;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hsappdev.ahs.dataTypes.CategoryList;
import com.hsappdev.ahs.dataTypes.CategoryListDAO;

public class CategoryListRepository {
    private CategoryListDAO categoryListDAO;

    private Application application;

    public CategoryListRepository(Application application) {
        this.application = application;
        RoomDatabase db = RoomDatabase.getDatabase(application);
        categoryListDAO = db.categoryListDAO();
    }

    public void add(CategoryList... categoryList) {
        RoomDatabase.databaseWriteExecutor.execute(() -> categoryListDAO.add(categoryList));
    }

    public LiveData<CategoryList> getCategory(String ID) {
        return categoryListDAO.getCategory(ID);
    }

    public void delete(String ID) {
        RoomDatabase.databaseWriteExecutor.execute(() -> categoryListDAO.delete(ID));
    }

    public Application getApplication() {
        return application;
    }
}

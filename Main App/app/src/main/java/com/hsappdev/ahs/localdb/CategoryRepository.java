package com.hsappdev.ahs.localdb;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.dataTypes.CategoryDAO;

import java.util.List;

public class CategoryRepository {
    private CategoryDAO categoryDAO;

    private Application application;

    public CategoryRepository(Application application) {
        this.application = application;
        RoomDatabase db = RoomDatabase.getDatabase(application);
        categoryDAO = db.categoryDAO();
    }

    public void add(Category... articles) {
        RoomDatabase.databaseWriteExecutor.execute(() -> categoryDAO.add(articles));
    }

    public LiveData<Category> getCategory(String ID) {
        return categoryDAO.getCategory(ID);
    }

    public void delete(String articleID) {
        RoomDatabase.databaseWriteExecutor.execute(() -> categoryDAO.delete(articleID));
    }

    public Application getApplication() {
        return application;
    }
}

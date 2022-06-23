package com.hsappdev.ahs.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hsappdev.ahs.newDataTypes.ArticleDataType;

import java.util.List;

public class ArticleRepository {
    private ArticleDAO articleDAO;
    private LiveData<List<ArticleDataType>> allArticles;
    private LiveData<List<ArticleDataType>> allSavedArticles;
    private LiveData<List<ArticleDataType>> allNotificationArticles;

    private Application application;

    public ArticleRepository(Application application) {
        this.application = application;
        RoomDatabase db = RoomDatabase.getDatabase(application);
        articleDAO = db.articleDAO();
        allArticles = articleDAO.getAllArticles();
        allSavedArticles = articleDAO.getAllSavedArticles();
        allNotificationArticles = articleDAO.getAllNotificationArticles();
    }

    public void addOrUpdate(ArticleDataType... articles) {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.addOrUpdateArticles(articles));
    }

    public LiveData<ArticleDataType> getArticle(String ID) {
        return articleDAO.getArticle(ID);
    }

    public void delete(String articleID) {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.deleteArticle(articleID));
    }

    public void deleteAll() {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.deleteAll());
    }

    // GETTERS AND SETTERS


    public LiveData<List<ArticleDataType>> getAllArticles() {
        return allArticles;
    }

    public LiveData<List<ArticleDataType>> getAllSavedArticles() {
        return allSavedArticles;
    }

    public LiveData<List<ArticleDataType>> getAllNotificationArticles() {
        return allNotificationArticles;
    }
}

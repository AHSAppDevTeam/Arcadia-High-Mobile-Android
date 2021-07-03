package com.hsappdev.ahs.localdb;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.ArticleDAO;

import java.util.List;

public class ArticleRepository {
    private ArticleDAO articleDAO;
    private LiveData<List<Article>> allArticles;

    public ArticleRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        articleDAO = db.articleDAO();
        allArticles = articleDAO.getAllSavedArticles();
    }

    public LiveData<List<Article>> getAllArticles() {return allArticles;}

    public LiveData<Boolean> isArticleSaved(String articleID) {
        return articleDAO.isArticleSaved(articleID);
    }

    public void add(Article... articles) {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.add(articles));
    }

    public void delete(String articleID) {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.delete(articleID));
    }

    public void deleteAll() {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.deleteAll());
    }

    public void updateArticle(Article article) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Article> itemsFromDB = articleDAO.getPossibleArticles(article.getArticleID());
                if (itemsFromDB.isEmpty())
                    add(article);
                else
                    articleDAO.updateSaved(article.getArticleID(), article.getIsSaved());
            }
        }).start();

    }
}

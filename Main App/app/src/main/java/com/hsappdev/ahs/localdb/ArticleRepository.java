package com.hsappdev.ahs.localdb;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.ArticleDAO;

import java.util.List;

public class ArticleRepository {
    private ArticleDAO articleDAO;
    private LiveData<List<Article>> allArticles;
    private LiveData<List<Article>> allSavedArticles;
    private LiveData<List<Article>> allNotificationArticles;


    public ArticleRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        articleDAO = db.articleDAO();
        allArticles = articleDAO.getAllArticles();
        allSavedArticles = articleDAO.getAllSavedArticles();
        allNotificationArticles = articleDAO.getAllNotificationArticles();
    }

    public LiveData<List<Article>> getAllArticles() {return allArticles;}

//    public Article getCachedArticle(String id) {
//        List<Article> articleList = articleDAO.getPossibleArticles(id);
//        if(articleList.isEmpty()){
//            return null;
//        } else {
//            return articleList.get(0);
//        }
//    }

    public LiveData<Boolean> isArticleSaved(String articleID) {
        return articleDAO.isArticleSaved(articleID);
    }

    public void add(Article... articles) {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.add(articles));
    }

    public LiveData<Article> getArticle(String ID) {
        return articleDAO.getArticle(ID);
    }

    public void delete(String articleID) {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.delete(articleID));
    }

    public void deleteAll() {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.deleteAll());
    }

    public void updateArticleFull(String articleID, String author, String title, String body, String categoryID, String[] imageURLs, String[] videoURLs, long timestamp, String categoryDisplayName, int categoryDisplayColor) {
        RoomDatabase.databaseWriteExecutor.execute(() -> articleDAO.updateArticleFull(articleID, author, title, body, categoryID,/* imageURLs, videoURLs,*/ timestamp, categoryDisplayName, categoryDisplayColor));
    }

    public void updateArticleSimple(Article article) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Article> itemsFromDB = articleDAO.getPossibleArticles(article.getArticleID());
                if (itemsFromDB.isEmpty())
                    add(article);
                else
                    articleDAO.updateArticleSimple(article.getArticleID(), article.getIsSaved(), article.getIsNotification());
            }
        }).start();

    }


    public LiveData<List<Article>> getAllSavedArticles() {
        return allSavedArticles;
    }

    public LiveData<List<Article>> getAllNotificationArticles() {
        return allNotificationArticles;
    }
}

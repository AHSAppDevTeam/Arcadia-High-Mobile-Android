package com.hsappdev.ahs.cache;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.ArrayList;

public class ArticleLoadableCache extends LoadableCache<Article> implements LoadableCallback{
    private final ArticleRepository articleRepository;
    private static final String TAG = "ArticleLoadableCache";
    public ArticleLoadableCache(String articleID, Resources r, LoadableCallback callback, ArticleRepository articleRepository) {
        super(articleID, r);
        this.articleRepository = articleRepository;
        registerForCallback(callback); // Make sure to do this first before loading articles
        startDataBaseLoad();
        startFirebaseLoad();
    }


    @Override
    protected LiveData<Article> getDatabaseLiveDataRef() {
        Log.d(TAG, "init2: " + articleRepository);

        return articleRepository.getArticle(articleID);
    }

    @Override
    protected DatabaseReference getFirebaseRef() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_articles))
                .child(articleID);
        return ref;
    }


    @Override
    protected Article getArticleInstance() {
        return new Article();
    }

    @Override
    protected void addCacheToDatabase() {
        articleRepository.add(article);
    }

    @Override
    protected boolean postFirebaseLoad() {
        // For Finding The Correct Color and Title for Featured Articles
        CategoryLoaderBackend.getInstance(articleRepository.getApplication()).getCacheObject(article.getCategoryID(), r, this);
        return true;
    }

    @Override
    protected void extractFirebaseValuesAndSetToObject(DataSnapshot snapshot) {
        String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
        String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
        String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
        String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
        ArrayList<String> imageURLs = new ArrayList<>();
        ArrayList<String> videoURLs = new ArrayList<>();
        for (DataSnapshot imageURL : snapshot.child(r.getString(R.string.db_articles_imageURLs)).getChildren()) {
            imageURLs.add(imageURL.getValue(String.class));
        }
        for (DataSnapshot videoURL : snapshot.child(r.getString(R.string.db_articles_videoURLs)).getChildren()) {
            videoURLs.add(videoURL.getValue(String.class));
        }
        boolean featured = true;

        long timestamp = snapshot.child(r.getString(R.string.db_articles_timestamp)).getValue(long.class);
        article.setArticleID(articleID);
        article.setAuthor(author);
        article.setTitle(title);
        article.setBody(body);
        article.setCategoryID(category);
        article.setImageURLs(imageURLs.toArray(new String[0]));
        article.setVideoURLs(videoURLs.toArray(new String[0]));
        article.setFeatured(featured);
        article.setTimestamp(timestamp);

    }

    @Override
    protected void updateArticleWithAdditionalDatabaseData(Article newArticle) {
        article.setIsViewed(newArticle.getIsViewed());
        article.setIsSaved(newArticle.getIsSaved());
        article.setIsNotification(newArticle.getIsNotification());
    }

    @Override
    public <T> void onLoaded(T articleN) {
        Category category = (Category) articleN;
        article.setCategoryDisplayName(category.getTitle());
        article.setCategoryDisplayColor(category.getColor());
        finalizeFirebaseLoad(); // MAKE SURE TO CALL THIS
    }

    @Override
    public boolean isActivityDestroyed() {
        return false;
    }
}

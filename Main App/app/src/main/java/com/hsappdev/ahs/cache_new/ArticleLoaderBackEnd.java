package com.hsappdev.ahs.cache_new;

import android.content.res.Resources;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.ArrayList;

public class ArticleLoaderBackEnd extends DataLoaderBackEnd<Article>{
    private Resources r;
    private ArticleRepository repository;

    public ArticleLoaderBackEnd(String dataID, Resources resources, ArticleRepository repository) {
        super(dataID);
        this.r = resources;
        this.repository = repository;
        startDatabaseLoad();
    }

    @Override
    protected LiveData<Article> getLocalData(String dataID) {
        return repository.getArticle(dataID);
    }

    @Override
    protected DatabaseReference getFirebaseRef(String dataID) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_articles))
                .child(dataID);
        return ref;
    }

    @Override
    protected Article getData_fromDataSnapshot(DataSnapshot snapshot, String dataID) {
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

        Article article = new Article();
        article.setArticleID(dataID);
        article.setAuthor(author);
        article.setTitle(title);
        article.setBody(body);
        article.setCategoryID(category);
        article.setImageURLs(imageURLs.toArray(new String[0]));
        article.setVideoURLs(videoURLs.toArray(new String[0]));
        article.setFeatured(featured);
        article.setTimestamp(timestamp);
        return article;
    }

    @Override
    protected void updateFirebaseData_withLocalData(Article firebaseData, Article localData) {
        firebaseData.setIsViewed(localData.getIsViewed());
        firebaseData.setIsSaved(localData.getIsSaved());
        firebaseData.setIsNotification(localData.getIsNotification());
    }

    @Override
    protected void updateFirebaseData_withDefaultLocalAttrs(Article firebaseData) {
        firebaseData.setIsViewed(0);
        firebaseData.setIsSaved(0);
        firebaseData.setIsNotification(0);
    }

    @Override
    protected void updateLocalDatabase(Article data) {
        repository.add(data);
    }
}

package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * A very useful class that can be used to load an article
 * This class prevents duplicate code and simplifies article loading
 * If there is an update to an article, the callback will be triggered a second time
 * @apiNote
 * ArticleLoader.getInstance().getArticle(String articleID, Resources r, OnArticleLoadedCallback callback)
 * @author Jeffrey Aaron Jeyasingh
 */

public class ArticleLoader {

    private List<ArticleCache> articleCache = new ArrayList<>();
    private List<Boolean> articleListenersSet = new ArrayList<>();


    private ArticleLoader() { }

    private static ArticleLoader articleLoader;

    public static ArticleLoader getInstance() {
        if(articleLoader == null){
            articleLoader = new ArticleLoader();
        }
        return articleLoader;
    }

    public void getArticle(String articleID, Resources r, OnArticleLoadedCallback callback) {
        // search the cache for the id
        boolean foundArticle = false;
        for (int i = 0; i < articleCache.size(); i++) {
            ArticleCache testArticle = articleCache.get(i);
            if(testArticle.articleID.equals(articleID)) {
                foundArticle = true;
                // registerForCallback is essential for the cache system
                // it handles whether an article should be taken from cache or from firebase
                testArticle.registerForCallback(callback);
            }
        }
        if(!foundArticle) {
            // If article is not in the cache, add it to the cache
            // each articleCache will take care of loading itself
            ArticleCache cacheArticle = new ArticleCache(articleID, r, callback);
            articleCache.add(cacheArticle);
        }
    }




    public class ArticleCache {
        private Article article;
        private List<OnArticleLoadedCallback> registeredCallbacks = new ArrayList<>();
        private final Resources r;
        private final String articleID;

        public ArticleCache(String articleID, Resources r) {
            this.r = r;
            this.articleID = articleID;
            loadArticle();
        }

        public ArticleCache(String articleID, Resources r, OnArticleLoadedCallback callback) {
            this.r = r;
            this.articleID = articleID;
            registerForCallback(callback); // Make sure to do this first before loading articles
            loadArticle();
        }

        public Article getArticle() {
            return article;
        }

        public void loadArticle() {
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(r.getString(R.string.db_articles))
                    .child(articleID);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                    String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                    String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                    String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
                    ArrayList<String> imageURLs = new ArrayList<>();
                    ArrayList<String> videoURLs = new ArrayList<>();
                    for(DataSnapshot imageURL: snapshot.child(r.getString(R.string.db_articles_imageURLs)).getChildren()) {
                        imageURLs.add(imageURL.getValue(String.class));
                    }
                    for (DataSnapshot videoURL : snapshot.child(r.getString(R.string.db_articles_videoURLs)).getChildren()) {
                        videoURLs.add(videoURL.getValue(String.class));
                    }
                    boolean featured = true;

                    long timestamp =  snapshot.child(r.getString(R.string.db_articles_timestamp)).getValue(long.class);

                    article = new Article(articleID, author, title, body, category, imageURLs.toArray(new String[0]), videoURLs.toArray(new String[0]), featured, timestamp);

                    Log.d("ArticleLoader", "load from firebase " + articleID);

                    boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
                    // For Finding The Correct Color and Title for Featured Articles
                    DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                            .child(r.getString(R.string.db_categories))
                            .child(article.getCategoryID());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);

                            int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));
                            article.setCategoryDisplayName(title);
                            article.setCategoryDisplayColor(color);
                            for(OnArticleLoadedCallback callback : registeredCallbacks)
                            if(!callback.isActivityDestroyed()) {
                                callback.onArticleLoaded(article);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public List<OnArticleLoadedCallback> getRegisteredCallbacks() {
            return registeredCallbacks;
        }

        public void setRegisteredCallbacks(List<OnArticleLoadedCallback> registeredCallbacks) {
            this.registeredCallbacks = registeredCallbacks;
        }

        public void registerForCallback(OnArticleLoadedCallback newCallback) {
            boolean isAlreadyRegistered = false;
            for(OnArticleLoadedCallback callback : registeredCallbacks){
                if(callback.equals(newCallback)){
                    isAlreadyRegistered = true;
                }
            }
            if(!isAlreadyRegistered) {
                registeredCallbacks.add(newCallback);
            }
            if(article != null) {
                // If the article is already loaded, call the callback
                // otherwise, wait until the callback is called in the onDataChange() method
                newCallback.onArticleLoaded(article);
            }
        }
    }
}


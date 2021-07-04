package com.hsappdev.ahs.cache;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A very useful class that can be used to load an article
 * This class prevents duplicate code and simplifies article loading
 * If there is an update to an article, the callback will be triggered a second time
 * This class support caching of article data and is very efficient
 * it prevents unnecessary firebase loading
 * @apiNote
 * ArticleLoader.getInstance().getArticle(String articleID, Resources r, OnArticleLoadedCallback callback)
 * @author Jeffrey Aaron Jeyasingh
 */

public class ArticleLoader {

    private static final String TAG = "ArticleLoader";

    private HashMap<String, ArticleCache> articleCache = new HashMap<>();
    private Application application;
    private ArticleRepository articleRepository;
    private List<Article> localDBArticleList = new ArrayList<>();

    private ArticleLoader(Application application) {
        this.application = application;
        articleRepository = new ArticleRepository(application);
    }

    private static ArticleLoader articleLoader;

    public static ArticleLoader getInstance(Application application) {
        if(articleLoader == null){
            articleLoader = new ArticleLoader(application);
        }
        return articleLoader;
    }

    /**
     * Developer purposes only
     */
    public void hardReloadAllArticles() {
        for (int i = 0; i < articleCache.size(); i++) {
            ArticleCache article = articleCache.get(i);
            article.reset();
        }
    }

    public void getArticle(String articleID, Resources r, OnArticleLoadedCallback callback) {
        Log.d(TAG, "getArticle: ask to load");
        // search the cache for the id
        ArticleCache article = articleCache.get(articleID);
        // registerForCallback is essential for the cache system
        // it handles whether an article should be taken from cache or from firebase
        if(article != null) {
            article.registerForCallback(callback);
            return;
        }

            // If article is not in the cache, add it to the cache
            // each articleCache will take care of loading itself
            ArticleCache cacheArticle = new ArticleCache(articleID, r, callback);
            articleCache.put(articleID, cacheArticle);

    }




    public class ArticleCache implements OnCategoryLoadedCallback {
        private Article article;
        private List<OnArticleLoadedCallback> registeredCallbacks = new ArrayList<>();
        private final Resources r;
        private final String articleID;
        private ValueEventListener valueEventListener;
        private DatabaseReference reference;
        private volatile boolean isInDatabase = false;

        public ArticleCache(String articleID, Resources r) {
            this.r = r;
            this.articleID = articleID;
            startDBLoad();
            loadArticle();
        }

        public ArticleCache(String articleID, Resources r, OnArticleLoadedCallback callback) {
            this.r = r;
            this.articleID = articleID;
            registerForCallback(callback); // Make sure to do this first before loading articles
            startDBLoad();
            loadArticle();
        }

        public ArticleCache(String articleID, Resources r, OnArticleLoadedCallback callback, Article article) {
            this(articleID, r);
            this.article = article;
            registerForCallback(callback);
            startDBLoad();
            loadArticle();
        }

        private void startDBLoad() {
            articleRepository.getArticle(articleID).observeForever(new Observer<Article>() {
                @Override
                public void onChanged(Article article) {
                    if(article != null) {
                        isInDatabase = true;
                        ArticleCache.this.article = article;

                        Log.d(TAG, "onCategoryLoaded: " + article.toString());

                        for (OnArticleLoadedCallback callback : registeredCallbacks) {
                            if (!callback.isActivityDestroyed()) {
                                callback.onArticleLoaded(article);
                            }
                        }
                        Log.d(TAG, "onChanged: db load");
                    }

                }
            });
        }


        public Article getArticle() {
            return article;
        }

        public void loadArticle() {
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                    .child(r.getString(R.string.db_articles))
                    .child(articleID);
            reference = ref;
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: firebase load");
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

                    //Log.d("ArticleLoader", "load from firebase " + articleID);

                    // For Finding The Correct Color and Title for Featured Articles
                    CategoryLoader.getInstance().getCategory(article.getCategoryID(), r, ArticleCache.this);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            valueEventListener = eventListener;
            ref.addValueEventListener(eventListener);
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

        public void reset() {
            article = null;
            reference.removeEventListener(valueEventListener);
            loadArticle();
        }

        @Override
        public void onCategoryLoaded(Category category) {
            article.setCategoryDisplayName(category.getTitle());
            article.setCategoryDisplayColor(category.getColor());
            for(OnArticleLoadedCallback callback : registeredCallbacks) {
                if (!callback.isActivityDestroyed()) {
                    callback.onArticleLoaded(article);
                }
            }

            if(!isInDatabase){
                articleRepository.add(article);
            }

            if(article != null) {
                articleRepository.updateArticleFull(articleID,
                        article.getAuthor(),
                        article.getTitle(),
                        article.getBody(),
                        article.getCategoryID(),
                        article.getImageURLs(),
                        article.getVideoURLs(),
                        article.getTimestamp(),
                        article.getCategoryDisplayName(),
                        article.getCategoryDisplayColor()
                );
            }
        }
    }
}


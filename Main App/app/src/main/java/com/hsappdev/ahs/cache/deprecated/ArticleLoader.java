package com.hsappdev.ahs.cache.deprecated;

import android.app.Application;
import android.content.res.Resources;
import android.os.Looper;
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
 * A very useful class that can be used to load an article <br>
 * This class prevents duplicate code and simplifies article loading <br>
 * If there is an update to an article, the callback will be triggered a second time <br>
 * This class support caching of article data and is very efficient <br>
 * it prevents unnecessary firebase loading <br>
 * New! Also stores articles to cache so articles can be loaded offline <br><br>
 * How the ArticleLoader works:
 * <ol>
 *     <li>When an article is asked for through the {@link #getArticle(String, Resources, OnArticleLoadedCallback)}
 *     method, both firebase and sqlite article loading are launched simultaneously</li>
 *     <li>The db is usually faster and its (possibly outdated) article value to the callback</li>
 *     <li>After firebase load finishes, its data is used to update the article in the database</li>
 * </ol>
 *
 *
 * @apiNote
 * ArticleLoader.getInstance().getArticle(String articleID, Resources r, OnArticleLoadedCallback callback)
 * @author Jeffrey Aaron Jeyasingh
 */
@Deprecated
public class ArticleLoader {

    private static final String TAG = "ArticleLoader";

    private HashMap<String, ArticleCache> articleCache = new HashMap<>();
    private Application application;
    private ArticleRepository articleRepository;

    private ArticleLoader(Application application) {
        this.application = application;
        articleRepository = new ArticleRepository(application);
//        // Start article load
//        loadArticlesFromDB();
        // For one time on app launch, clean up unused articles
        trimUnusedArticles();
    }

    long start;
//
//    private void loadArticlesFromDB() {
//        start = System.currentTimeMillis();
//        articleRepository.getAllArticles().observeForever(new Observer<List<Article>>() {
//            @Override
//            public void onChanged(List<Article> articleList) {
//                for(Article a : articleList) {
//                    ArticleCache ac = articleCache.get(a.getArticleID());
//                    if(ac != null) {
//                        ac.onDBLoad(a);
//                    }
//                }
//            }
//        });
//    }

    private void trimUnusedArticles() {
        //new Handler().post(new LocalDBArticleTrimmer(r));
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
            if (article != null) {
                article.reset();
            }
        }
    }

    @Deprecated
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
        private volatile boolean hasFirebaseLoadFinished = false;



        public ArticleCache(String articleID, Resources r, OnArticleLoadedCallback callback) {
            this.r = r;
            this.articleID = articleID;
            registerForCallback(callback); // Make sure to do this first before loading articles
            startDBLoad();
            loadArticle();
        }


//        private void onDBLoad(Article article) {
//            if(hasFirebaseLoadFinished){
//                return;
//            }
//            if(article != null) {
//                isInDatabase = true;
//                ArticleCache.this.article = article;
//
//                Log.d(TAG, "onCategoryLoaded: " + article.toString());
//
//                for (OnArticleLoadedCallback callback : registeredCallbacks) {
//                    if (!callback.isActivityDestroyed()) {
//                        callback.onArticleLoaded(article);
//                    }
//                }
//                Log.d(TAG, "onChanged: db load " + (System.currentTimeMillis()-start));
//            }
//        }


        private void startDBLoad() {
            if(Looper.getMainLooper().getThread() != Thread.currentThread()) {
                // If we are not on the main thread, we cannot call observe forever
                // so we fall back to firebase loading
                return;
            }

            articleRepository.getArticle(articleID).observeForever(new Observer<Article>() {
                @Override
                public void onChanged(Article articleN) {
                    if(articleN != null) {
                        isInDatabase = true;
                        if(hasFirebaseLoadFinished){
                            article.setIsViewed(articleN.getIsViewed());
                            article.setIsSaved(articleN.getIsSaved());
                            article.setIsNotification(articleN.getIsNotification());
                            return;
                        }
                        ArticleCache.this.article = articleN;

                        Log.d(TAG, "onCategoryLoaded: " + articleN.toString());

                        for (OnArticleLoadedCallback callback : registeredCallbacks) {
                            if (!callback.isActivityDestroyed()) {
                                callback.onArticleLoaded(articleN);
                            }
                        }
                        Log.d(TAG, "onChanged: db load " + (System.currentTimeMillis()-start));
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
                    String isViewed = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
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

                    if(article == null)
                        article = new Article(articleID, author, title, body, category, imageURLs.toArray(new String[0]), videoURLs.toArray(new String[0]), featured, timestamp);
                    else {
                        // Prevent losing the notif, saved, and viewed flags
                        article.setAuthor(author);
                        article.setTitle(title);
                        article.setBody(body);
                        article.setCategoryID(category);
                        article.setImageURLs(imageURLs.toArray(new String[0]));
                        article.setVideoURLs(videoURLs.toArray(new String[0]));
                        article.setFeatured(featured);
                        article.setTimestamp(timestamp);

                    }

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
            hasFirebaseLoadFinished = true;
            // If firebase is faster and the article is loaded from firebase
            // we set this var to make sure that the latest data from firebase is not updated with old
            // data from the cache

            if(!isInDatabase){
                articleRepository.add(article);
            }

            if(article != null) {
                System.out.println("reload");
                articleRepository.add(article);
            }
        }
    }
}


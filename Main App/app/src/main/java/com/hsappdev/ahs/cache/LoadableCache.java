package com.hsappdev.ahs.cache;

import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadableCache<T extends LoadableType> {

    private static final String TAG = "LoadableCache";

    protected T article;
    protected List<LoadableCallback<T>> registeredCallbacks = new ArrayList<>();
    protected final Resources r;
    protected final String articleID;
    protected ValueEventListener valueEventListener;
    protected DatabaseReference reference;
    protected volatile boolean isInDatabase = false;
    protected volatile boolean hasFirebaseLoadFinished = false;

    public LoadableCache(String articleID, Resources r) {
        this.r = r;
        this.articleID = articleID;
    }

    public LoadableCache(String articleID, Resources r, LoadableCallback<T> callback) {
        this(articleID, r);
        registerForCallback(callback); // Make sure to do this first before loading articles
        startDataBaseLoad();
        startFirebaseLoad();
    }

    protected abstract LiveData<T> getDatabaseLiveDataRef();

    protected abstract DatabaseReference getFirebaseRef();

    public T getArticle() {
        return article;
    }


    public void registerForCallback(LoadableCallback<T> newCallback) {
        boolean isAlreadyRegistered = false;
        for(LoadableCallback<T> callback : registeredCallbacks){
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
            newCallback.onLoaded(article);
        }
    }

    void startFirebaseLoad() {
        reference = getFirebaseRef();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: firebase load");

                if (article == null) {
                    article = getArticleInstance();

                }

                extractFirebaseValuesAndSetToObject(snapshot);

                if(!postFirebaseLoad()) {
                    finalizeFirebaseLoad();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        valueEventListener = eventListener;
        reference.addValueEventListener(eventListener);
    }

    protected abstract T getArticleInstance();

    protected void finalizeFirebaseLoad() {
        for(LoadableCallback<T> callback : registeredCallbacks) {
            if (!callback.isActivityDestroyed()) {
                callback.onLoaded(article);
            }
        }
        hasFirebaseLoadFinished = true;
        // If firebase is faster and the article is loaded from firebase
        // we set this var to make sure that the latest data from firebase is not updated with old
        // data from the cache

        if(!isInDatabase){
            addCacheToDatabase();
        }

        if(article != null) {
            addCacheToDatabase();
        }
    }

    protected abstract void addCacheToDatabase();

    protected abstract boolean postFirebaseLoad();

    protected abstract void extractFirebaseValuesAndSetToObject(DataSnapshot snapshot);
    /*
                String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
                String isViewed = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
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
                article.setAuthor(author);
                article.setTitle(title);
                article.setBody(body);
                article.setCategoryID(category);
                article.setImageURLs(imageURLs.toArray(new String[0]));
                article.setVideoURLs(videoURLs.toArray(new String[0]));
                article.setFeatured(featured);
                article.setTimestamp(timestamp);
     */

    void startDataBaseLoad() {
        LiveData<T> liveData = getDatabaseLiveDataRef();
        if(liveData == null) return;
        if(Looper.getMainLooper().getThread() != Thread.currentThread()) {
            // If we are not on the main thread, we cannot call observe forever
            // so we fall back to firebase loading
            return;
        }
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T articleN) {
                if(articleN != null) {
                    isInDatabase = true;
                    if(hasFirebaseLoadFinished){
                        updateArticleWithAdditionalDatabaseData(articleN);
                        return;
                    }
                    LoadableCache.this.article = articleN;

                    Log.d(TAG, "onCategoryLoaded: " + articleN.toString());

                    for (LoadableCallback<T> callback : registeredCallbacks) {
                        if (!callback.isActivityDestroyed()) {
                            callback.onLoaded(articleN);
                        }
                    }
                    Log.d(TAG, "onChanged: db load " + (System.currentTimeMillis()));
                }
            }
        });
    }


    protected abstract void updateArticleWithAdditionalDatabaseData(T newArticle);
    /*
    ex.
                        article.setIsViewed(articleN.getIsViewed());
                        article.setIsSaved(articleN.getIsSaved());
                        article.setIsNotification(articleN.getIsNotification());
     */

}

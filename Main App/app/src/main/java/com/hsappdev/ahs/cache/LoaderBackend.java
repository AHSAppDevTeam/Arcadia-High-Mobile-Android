package com.hsappdev.ahs.cache;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import java.util.HashMap;

public abstract class LoaderBackend<T extends LoadableCache<A>, A extends LoadableType> {

    protected static final String TAG = "LoaderBackend";

    protected HashMap<String, T> articleCache = new HashMap<>();
    protected Application application;

    public LoaderBackend(Application application) {
        this.application = application;
        init();
    }

    protected abstract void init();

    public void getCacheObject(String articleID, Resources r, LoadableCallback<A> callback) {
        if(articleID == null) {
            Log.e(TAG, "getCacheObject: ERROR null was passed to loader");
            return;
        }
        Log.d(TAG, "getArticle: ask to load");
        // search the cache for the id
        T article = articleCache.get(articleID);
        // registerForCallback is essential for the cache system
        // it handles whether an article should be taken from cache or from firebase
        if(article != null) {
            article.registerForCallback(callback);
            return;
        }

        // If article is not in the cache, add it to the cache
        // each articleCache will take care of loading itself
        T articleCacheInstance = getLoadableCacheInstance(articleID, r, callback);
        articleCache.put(articleID, articleCacheInstance);

    }

    protected abstract T getLoadableCacheInstance(String articleID, Resources r, LoadableCallback<A> callback);
}

package com.hsappdev.ahs.cache;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.ArticleRepository;

public class ArticleLoaderBackend extends LoaderBackend<ArticleLoadableCache, Article>{
    private ArticleRepository articleRepository;

    private static ArticleLoaderBackend articleLoaderBackend;

    public static ArticleLoaderBackend getInstance(Application application) {
        if(articleLoaderBackend == null) {
            articleLoaderBackend = new ArticleLoaderBackend(application);
        }
        return articleLoaderBackend;
    }

    public ArticleLoaderBackend(Application application) {
        super(application);
    }

    @Override
    protected void init() {
        articleRepository = new ArticleRepository(application);
        Log.d(TAG, "init: " + articleRepository);
    }

    @Override
    protected ArticleLoadableCache getLoadableCacheInstance(String articleID, Resources r, LoadableCallback<Article> callback) {
        Log.d(TAG, "init: " + articleRepository);
        return new ArticleLoadableCache(articleID, r, callback, articleRepository);
    }
}

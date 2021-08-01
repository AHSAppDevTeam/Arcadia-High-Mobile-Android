package com.hsappdev.ahs.cache;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.deprecated.OnCategoryLoadedCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.List;

public class LocalDBArticleTrimmer implements Runnable, LoadableCallback {
    private static final String TAG = "LocalDBArticleTrimmer";
    private final Resources r;
    private final Application application;
    private final ArticleRepository articleRepository;
    private int index = 0; // 0=home, 1=bulletin, 2=community

    public LocalDBArticleTrimmer(Resources r, Application application) {
        this.r = r;
        this.application = application;
        this.articleRepository = new ArticleRepository(application);
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_LOWEST);
        Log.d(TAG, "run: start db article trimming");
        CategoryLoaderBackend.getInstance(application).getCacheObject(r.getString(R.string.db_location_ausdNews), r, this);
        CategoryLoaderBackend.getInstance(application).getCacheObject(r.getString(R.string.db_location_bulletin), r, this);
        CategoryLoaderBackend.getInstance(application).getCacheObject(r.getString(R.string.db_location_community), r, this);
    }


    @Override
    public <T> void onLoaded(T article) {
        Category category = (Category) article;
        List<String> articleIds = category.getArticleIds();
        articleRepository.getAllArticles().observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleList) {
                for (Article a : articleList) {
                    for(String aNID : articleIds) {

                    }
                }
            }
        });

    }

    @Override
    public boolean isActivityDestroyed() {
        return index>=2;
    }
}

package com.hsappdev.ahs.cache;

import com.hsappdev.ahs.dataTypes.Article;

public interface OnArticleLoadedCallback {

    Article article = null;

    void onArticleLoaded(Article article);

    boolean isActivityDestroyed();

}

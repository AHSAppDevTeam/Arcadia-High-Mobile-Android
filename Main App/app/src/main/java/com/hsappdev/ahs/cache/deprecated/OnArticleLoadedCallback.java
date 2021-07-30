package com.hsappdev.ahs.cache.deprecated;

import com.hsappdev.ahs.dataTypes.Article;
@Deprecated
public interface OnArticleLoadedCallback {

    Article article = null;

    void onArticleLoaded(Article article);

    boolean isActivityDestroyed();

}

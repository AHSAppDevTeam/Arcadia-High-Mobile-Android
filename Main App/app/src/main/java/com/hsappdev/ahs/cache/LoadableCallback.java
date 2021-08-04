package com.hsappdev.ahs.cache;

public interface LoadableCallback<T extends LoadableType> {
    void onLoaded(T article);

    boolean isActivityDestroyed();
}

package com.hsappdev.ahs.cache;

public interface LoadableCallback {
    <T> void onLoaded(T article);

    boolean isActivityDestroyed();
}

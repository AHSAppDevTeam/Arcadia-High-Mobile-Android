package com.hsappdev.ahs.cache;

import android.app.Application;
import android.content.res.Resources;

public class CategoryLoaderBackend extends LoaderBackend<CategoryLoadableCache>{

    private static CategoryLoaderBackend categoryLoaderBackend;

    public static CategoryLoaderBackend getInstance(Application application) {
        if(categoryLoaderBackend == null) {
            categoryLoaderBackend = new CategoryLoaderBackend(application);
        }
        return categoryLoaderBackend;
    }

    public CategoryLoaderBackend(Application application) {
        super(application);
    }

    @Override
    protected void init() {

    }

    @Override
    protected CategoryLoadableCache getLoadableCacheInstance(String articleID, Resources r, LoadableCallback callback) {
        return new CategoryLoadableCache(articleID, r, callback);
    }
}

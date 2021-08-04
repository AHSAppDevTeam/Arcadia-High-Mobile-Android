package com.hsappdev.ahs.cache;

import android.app.Application;
import android.content.res.Resources;

import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.localdb.CategoryRepository;

public class CategoryLoaderBackend extends LoaderBackend<CategoryLoadableCache, Category>{

    private static CategoryLoaderBackend categoryLoaderBackend;
    private CategoryRepository categoryRepository;

    public static CategoryLoaderBackend getInstance(Application application) {
        if(categoryLoaderBackend == null) {
            categoryLoaderBackend = new CategoryLoaderBackend(application);
        }
        return categoryLoaderBackend;
    }

    public CategoryLoaderBackend(Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
    }

    @Override
    protected void init() {

    }

    @Override
    protected CategoryLoadableCache getLoadableCacheInstance(String articleID, Resources r, LoadableCallback<Category> callback) {
        return new CategoryLoadableCache(articleID, r, callback, categoryRepository);
    }
}

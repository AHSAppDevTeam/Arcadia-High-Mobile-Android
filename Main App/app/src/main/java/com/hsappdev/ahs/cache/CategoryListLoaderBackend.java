package com.hsappdev.ahs.cache;

import android.app.Application;
import android.content.res.Resources;

import com.hsappdev.ahs.dataTypes.CategoryList;
import com.hsappdev.ahs.localdb.CategoryListRepository;

public class CategoryListLoaderBackend extends LoaderBackend<CategoryListLoadableCache, CategoryList>{

    private static CategoryListLoaderBackend categoryLoaderBackend;
    private CategoryListRepository categoryListRepository;

    public static CategoryListLoaderBackend getInstance(Application application) {
        if(categoryLoaderBackend == null) {
            categoryLoaderBackend = new CategoryListLoaderBackend(application);
        }
        return categoryLoaderBackend;
    }

    public CategoryListLoaderBackend(Application application) {
        super(application);
        categoryListRepository = new CategoryListRepository(application);
    }

    @Override
    protected void init() {

    }

    @Override
    protected CategoryListLoadableCache getLoadableCacheInstance(String articleID, Resources r, LoadableCallback<CategoryList> callback) {
        return new CategoryListLoadableCache(articleID, r, callback, categoryListRepository);
    }
}

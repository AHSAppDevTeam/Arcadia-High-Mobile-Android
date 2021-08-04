package com.hsappdev.ahs.cache;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.CategoryList;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.localdb.CategoryListRepository;

import java.util.ArrayList;

public class CategoryListLoadableCache extends LoadableCache<CategoryList> {

    private final CategoryListRepository categoryListRepository;
    public CategoryListLoadableCache(String articleID, Resources r, LoadableCallback<CategoryList> callback, CategoryListRepository categoryListRepository) {
        super(articleID, r);
        this.categoryListRepository = categoryListRepository;
        registerForCallback(callback); // Make sure to do this first before loading categories
        startDataBaseLoad();
        startFirebaseLoad();
    }

    @Override
    protected LiveData getDatabaseLiveDataRef() {
        return categoryListRepository.getCategory(articleID);
    }

    @Override
    protected DatabaseReference getFirebaseRef() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_locations))
                .child(articleID)// homepage is default
                .child(r.getString(R.string.db_locations_catID));
        return ref;
    }

    @Override
    protected CategoryList getArticleInstance() {
        return new CategoryList();
    }

    @Override
    protected void addCacheToDatabase() {
        categoryListRepository.add(article);
    }

    @Override
    protected boolean postFirebaseLoad() {
        return false;
    }

    @Override
    protected void extractFirebaseValuesAndSetToObject(DataSnapshot snapshot) {
        ArrayList<String> categoriesIDs = new ArrayList<>();
        for (DataSnapshot sectionTitle : snapshot.getChildren()) {
            categoriesIDs.add(sectionTitle.getValue(String.class));
        }
        article.setCategorySection(articleID);
        article.setCategoryList(categoriesIDs);
    }

    @Override
    protected void updateArticleWithAdditionalDatabaseData(CategoryList newArticle) {

    }
}

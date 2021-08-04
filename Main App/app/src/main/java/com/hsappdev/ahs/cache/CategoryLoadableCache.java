package com.hsappdev.ahs.cache;

import android.content.res.Resources;
import android.graphics.Color;

import androidx.lifecycle.LiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.localdb.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class CategoryLoadableCache extends LoadableCache<Category> {
    private final CategoryRepository categoryRepository;
    public CategoryLoadableCache(String articleID, Resources r, LoadableCallback<Category> callback, CategoryRepository categoryRepository) {
        super(articleID, r);
        this.categoryRepository = categoryRepository;
        registerForCallback(callback); // Make sure to do this first before loading categories
        startDataBaseLoad();
        startFirebaseLoad();
    }

    @Override
    protected LiveData<Category> getDatabaseLiveDataRef() {
        return categoryRepository.getCategory(articleID);
    }

    @Override
    protected DatabaseReference getFirebaseRef() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_categories))
                .child(articleID);
        return ref;
    }

    @Override
    protected Category getArticleInstance() {
        return new Category();
    }

    @Override
    protected void addCacheToDatabase() {
        categoryRepository.add(article);
    }

    @Override
    protected boolean postFirebaseLoad() {
        return false;
    }

    @Override
    protected void extractFirebaseValuesAndSetToObject(DataSnapshot snapshot) {
        List<String> articleIds = new ArrayList<>();
        for(DataSnapshot articleId : snapshot.child(r.getString(R.string.db_categories_articleIds)).getChildren()){
            articleIds.add(articleId.getValue(String.class));
        }

        String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);
        int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));
        String iconURL = snapshot.child(r.getString(R.string.db_categories_iconURL)).getValue(String.class);
        article.setCategoryID(articleID);
        article.setColor(color);
        article.setTitle(title);
        article.setIconURL(iconURL);
        article.setArticleIds(articleIds);
    }

    @Override
    protected void updateArticleWithAdditionalDatabaseData(Category newArticle) {

    }

}

package com.hsappdev.ahs.cache_new;

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

public class CategoryLoaderBackEnd extends DataLoaderBackEnd<Category>{
    private Resources r;
    private CategoryRepository repository;
    public CategoryLoaderBackEnd(String dataID, Resources resources, CategoryRepository repository) {
        super(dataID);
        this.r = resources;
        this.repository = repository;
        startDatabaseLoad();
    }
    
    @Override
    protected LiveData<Category> getLocalData(String dataID) {
        return repository.getCategory(dataID);
    }

    @Override
    protected DatabaseReference getFirebaseRef(String dataID) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_categories))
                .child(dataID);
        return ref;
    }

    @Override
    protected Category getData_fromDataSnapshot(DataSnapshot snapshot, String dataID) {
        List<String> articleIds = new ArrayList<>();
        for(DataSnapshot articleId : snapshot.child(r.getString(R.string.db_categories_articleIds)).getChildren()){
            articleIds.add(articleId.getValue(String.class));
        }

        String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);
        int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));
        String iconURL = snapshot.child(r.getString(R.string.db_categories_iconURL)).getValue(String.class);
        Category category = new Category();
        category.setCategoryID(dataID);
        category.setColor(color);
        category.setTitle(title);
        category.setIconURL(iconURL);
        category.setArticleIds(articleIds);
        return category;
    }

    @Override
    protected void updateFirebaseData_withLocalData(Category firebaseData, Category localData) {

    }

    @Override
    protected void updateFirebaseData_withDefaultLocalAttrs(Category firebaseData) {

    }

    @Override
    protected void updateLocalDatabase(Category data) {
        repository.add(data);
    }
}

package com.hsappdev.ahs.cache_new;

import android.content.res.Resources;
import android.util.Log;

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

public class CategoryListLoaderBackEnd extends DataLoaderBackEnd<CategoryList> {
    private Resources r;
    private CategoryListRepository repository;

    public CategoryListLoaderBackEnd(String ID, Resources resources, CategoryListRepository repository) {
        super(ID);
        this.r = resources;
        this.repository = repository;
        startDatabaseLoad();
    }

    @Override
    protected LiveData<CategoryList> getLocalData(String dataID) {
        return repository.getCategory(dataID);
    }

    @Override
    protected DatabaseReference getFirebaseRef(String dataID) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_locations))
                .child(dataID)// homepage is default
                .child(r.getString(R.string.db_locations_catID));
        return ref;
    }

    @Override
    protected CategoryList getData_fromDataSnapshot(DataSnapshot snapshot, String dataID) {

        ArrayList<String> categoriesIDs = new ArrayList<>();
        for (DataSnapshot sectionTitle : snapshot.getChildren()) {
            categoriesIDs.add(sectionTitle.getValue(String.class));
        }
        CategoryList categoryList = new CategoryList();

        categoryList.setCategorySection(dataID);
        categoryList.setCategoryList(categoriesIDs);
        return categoryList;
    }

    @Override
    protected void updateFirebaseData_withLocalData(CategoryList firebaseData, CategoryList localData) {

    }

    @Override
    protected void updateFirebaseData_withDefaultLocalAttrs(CategoryList firebaseData) {

    }

    @Override
    protected void updateLocalDatabase(CategoryList data) {
        repository.add(data);
    }
}

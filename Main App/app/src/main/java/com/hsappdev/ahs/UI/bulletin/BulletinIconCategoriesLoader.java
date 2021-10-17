package com.hsappdev.ahs.UI.bulletin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.CategoryLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.cache.callbacks.CategoryLoadableCallback;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

public class BulletinIconCategoriesLoader {
    private final Resources r;
    private List<String> categories;
    public BulletinIconCategoriesLoader(Context context) {
        r = context.getResources();
    }

    public void loadCategories(CategoriesLoadedCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_locations))
                .child(r.getString(R.string.db_location_bulletin))
                .child(r.getString(R.string.db_locations_catID));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> categoriesIDs = new ArrayList<>();
                for(DataSnapshot category : snapshot.getChildren()) {
                    categoriesIDs.add(category.getValue(String.class));
                }
                categories = categoriesIDs;
                callback.onLoad(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadCategoryData(String categoryId, CategoryLoadableCallback callback, Activity activity) {
        CategoryLoaderBackend.getInstance(activity.getApplication()).getCacheObject(categoryId, activity.getResources(), callback);
    }

}

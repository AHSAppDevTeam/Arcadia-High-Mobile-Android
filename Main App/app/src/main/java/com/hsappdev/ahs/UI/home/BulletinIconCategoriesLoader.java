package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.bulletin.BulletinCategoryWidget;

import java.util.ArrayList;
import java.util.List;

public class BulletinIconCategoriesLoader {
    private final Resources r;
    private List<String> categories;
    private final CategoriesLoadedCallback categoriesLoadedCallback;
    public BulletinIconCategoriesLoader(Context context, CategoriesLoadedCallback callback) {
        r = context.getResources();
        this.categoriesLoadedCallback = callback;
        loadCategories();
    }

    private void loadCategories() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
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
                categoriesLoadedCallback.onLoad(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    interface CategoriesLoadedCallback {
        void onLoad(List<String> categories);
    }
}

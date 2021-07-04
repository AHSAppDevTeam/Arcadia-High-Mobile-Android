package com.hsappdev.ahs.UI.bulletin;

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

    public void loadCategoryData(String categoryId, CategoryLoadedCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(r.getString(R.string.db_categories))
                .child(categoryId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);
                int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));
                String iconURL = snapshot.child(r.getString(R.string.db_categories_iconURL)).getValue(String.class);
                List<String> articleIds = new ArrayList<>();
                for(DataSnapshot articleId : snapshot.child(r.getString(R.string.db_categories_articleIds)).getChildren()){
                    articleIds.add(articleId.getValue(String.class));
                }

                callback.onCategoryDataLoaded(new Category(categoryId, title, color, iconURL), articleIds);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

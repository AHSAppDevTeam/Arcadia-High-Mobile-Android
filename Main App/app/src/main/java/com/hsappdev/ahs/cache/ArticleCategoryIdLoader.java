package com.hsappdev.ahs.cache;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

public class ArticleCategoryIdLoader {
    public static List<String> articleCategoryCache = new ArrayList<>();

    public static void loadCategoryList(Resources r, OnCategoryListLoadedCallback callback){
        if(articleCategoryCache.size() > 0){
            callback.categoryListLoaded(articleCategoryCache);
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                    .child(r.getString(R.string.db_locations))
                    .child(r.getString(R.string.db_location_ausdNews))// homepage is default
                    .child(r.getString(R.string.db_locations_catID));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ArrayList<String> categoriesIDs = new ArrayList<>();
                    for (DataSnapshot sectionTitle : snapshot.getChildren()) {
                        categoriesIDs.add(sectionTitle.getValue(String.class));
                    }
                    articleCategoryCache = categoriesIDs;
                    callback.categoryListLoaded(articleCategoryCache);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}

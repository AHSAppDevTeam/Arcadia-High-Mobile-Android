package com.hsappdev.ahs.cache.deprecated;

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
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Similar in function to the ArticleLoader class
 * Caches category data
 * @apiNote
 * CategoryLoader.getInstance().getCategory(String categoryID, Resources r, OnCategoryLoadedCallback callback)
 * @author Jeffrey Aaron Jeyasingh
 */

@Deprecated
public class CategoryLoader {
    private static final String TAG = "ArticleListLoader";

    private HashMap<String, CategoryCache> articleCache = new HashMap<>();


    private CategoryLoader() { }

    private static CategoryLoader articleLoader;


    public static CategoryLoader getInstance() {
        if(articleLoader == null){
            articleLoader = new CategoryLoader();
        }
        return articleLoader;
    }

    @Deprecated
    /**
     * Use CategoryLoaderBackend instead
     * @see CategoryLoaderBackend
     */
    public void getCategory(String categoryID, Resources r, OnCategoryLoadedCallback callback) {
        // search the cache for the id
        CategoryCache categoryCache = articleCache.get(categoryID);
                // registerForCallback is essential for the cache system
                // it handles whether an article should be taken from cache or from firebase
        if(categoryCache != null) {
            categoryCache.registerForCallback(callback);
            return;
        }


            // If article is not in the cache, add it to the cache
            // each articleCache will take care of loading itself
            CategoryCache cacheArticle = new CategoryCache(categoryID, r, callback);
            articleCache.put(categoryID, cacheArticle);

    }

    public class CategoryCache {
        private Category category;
        private List<OnCategoryLoadedCallback> registeredCallbacks = new ArrayList<>();
        private final Resources r;
        private final String categoryID;

        public CategoryCache(String categoryID, Resources r, OnCategoryLoadedCallback callback) {
            this.r = r;
            this.categoryID = categoryID;
            registerForCallback(callback); // Make sure to do this first before loading articles
            loadCategory();
        }

        public void loadCategory() {
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                    .child(r.getString(R.string.db_categories))
                    .child(categoryID);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Log.d(TAG, "load from firebase: " + categoryID);
                    List<String> articleIds = new ArrayList<>();
                    for(DataSnapshot articleId : snapshot.child(r.getString(R.string.db_categories_articleIds)).getChildren()){
                        articleIds.add(articleId.getValue(String.class));
                    }

                    String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);
                    int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));
                    String iconURL = snapshot.child(r.getString(R.string.db_categories_iconURL)).getValue(String.class);
                    category = new Category(categoryID, title, color, iconURL, articleIds);
                    for(OnCategoryLoadedCallback callback : registeredCallbacks) {
                        callback.onCategoryLoaded(category);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void registerForCallback(OnCategoryLoadedCallback newCallback) {
            boolean isAlreadyRegistered = false;
            for(OnCategoryLoadedCallback callback : registeredCallbacks){
                if(callback.equals(newCallback)){
                    isAlreadyRegistered = true;
                }
            }
            if(!isAlreadyRegistered) {
                registeredCallbacks.add(newCallback);
            }
            if(category != null) {
                // If the category is already loaded, call the callback
                // otherwise, wait until the callback is called in the onDataChange() method
                newCallback.onCategoryLoaded(category);
            }
        }

    }
}

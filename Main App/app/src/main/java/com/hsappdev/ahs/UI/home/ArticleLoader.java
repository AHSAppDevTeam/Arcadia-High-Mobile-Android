package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

import java.util.ArrayList;

/**
 * A very useful class that can be used to load an article
 * This class prevents duplicate code and simplifies article loading
 * The class calling
 * new ArticleLoader().loadArticle()
 * should implement the OnArticleLoadedCallback
 * If there is an update to an article, the callback will be triggered a second time
 */

public class ArticleLoader {

    private Article article;

    public ArticleLoader() {
        super();
    }

    public void loadArticle(String articleID, Resources r, OnArticleLoadedCallback onArticleLoadedCallback) {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                .child(r.getString(R.string.db_articles))
                .child(articleID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
                ArrayList<String> imageURLs = new ArrayList<>();
                ArrayList<String> videoURLs = new ArrayList<>();
                for(DataSnapshot imageURL: snapshot.child(r.getString(R.string.db_articles_imageURLs)).getChildren()) {
                    imageURLs.add(imageURL.getValue(String.class));
                }
                for (DataSnapshot videoURL : snapshot.child(r.getString(R.string.db_articles_videoURLs)).getChildren()) {
                    videoURLs.add(videoURL.getValue(String.class));
                }
                boolean featured = true;

                long timestamp =  snapshot.child(r.getString(R.string.db_articles_timestamp)).getValue(long.class);

                article = new Article(articleID, author, title, body, category, imageURLs.toArray(new String[0]), videoURLs.toArray(new String[0]), featured, timestamp);


                boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
                // For Finding The Correct Color and Title for Featured Articles
                DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                        .child(r.getString(R.string.db_categories))
                        .child(article.getCategory());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);

                        int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));
                        article.setCategoryDisplayName(title);
                        article.setCategoryDisplayColor(color);
                        onArticleLoadedCallback.onArticleLoaded(article);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


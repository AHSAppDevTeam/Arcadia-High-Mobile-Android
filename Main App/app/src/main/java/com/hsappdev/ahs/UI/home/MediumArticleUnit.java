package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

import java.util.ArrayList;

public class MediumArticleUnit extends ConstraintLayout {
    private Article article;
    final private ConstraintLayout articleLayout;
    final private ImageView articleImage;
    final private Resources r;
    final private TextView titleTextView;
    final private TextView timeTextView;
    private OnItemClick onArticleClick;

    final private View contentView;


    public MediumArticleUnit(@NonNull Context context, String articleId, OnItemClick onItemClick, int layoutID) {
        super(context);
        // Inflate view
        View view = inflate(getContext(), layoutID, this);

        // Get the data
        this.r = view.getResources();
        this.articleLayout = view.findViewById(R.id.home_news_constraintLayout);
        this.articleImage = view.findViewById(R.id.medium_article_image);
        this.titleTextView = view.findViewById(R.id.medium_article_name);
        this.timeTextView = view.findViewById(R.id.medium_article_time);
        this.onArticleClick = onItemClick;
        contentView = view;
        setDetails(articleId);
    }

    public MediumArticleUnit(@NonNull Context context, String articleId, OnItemClick onItemClick) {
        super(context);
        // Inflate view
        View view = inflate(getContext(), R.layout.home_news_medium_article, this);

        // Get the data
        this.r = view.getResources();
        this.articleLayout = view.findViewById(R.id.home_news_constraintLayout);
        this.articleImage = view.findViewById(R.id.medium_article_image);
        this.titleTextView = view.findViewById(R.id.medium_article_name);
        this.timeTextView = view.findViewById(R.id.medium_article_time);
        this.onArticleClick = onItemClick;
        contentView = view;
        setDetails(articleId);
    }

    public void setDetails(String articleId){
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                .child(r.getString(R.string.db_articles))
                .child(articleId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
                ArrayList<String> imageURLs = new ArrayList<>();
                for (DataSnapshot imageURL : snapshot.child(r.getString(R.string.db_articles_imageURLs)).getChildren()) {
                    imageURLs.add(imageURL.getValue(String.class));
                }
                boolean featured = true;

                long timestamp = snapshot.child(r.getString(R.string.db_articles_timestamp)).getValue(long.class);

                article = new Article(articleId, author, title, body, category, imageURLs.toArray(new String[0]), featured, timestamp);

                titleTextView.setText(article.getTitle());
                ScreenUtil.setTimeToTextView(article.getTimestamp(), timeTextView);
                if(article.getImageURLs().length != 0){
                    ImageUtil.setImageToSmallView(article.getImageURLs()[0], articleImage);
                }

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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                contentView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        onArticleClick.onArticleClicked(article);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

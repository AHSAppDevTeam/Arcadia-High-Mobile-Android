package com.hsappdev.ahs.UI.home;

import android.app.Activity;
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

public class MediumArticleUnit extends ConstraintLayout implements OnArticleLoadedCallback{
    protected Article article;
    final private ConstraintLayout articleLayout;
    final private ImageView articleImage;
    final private Resources r;
    final private TextView titleTextView;
    final private TextView timeTextView;
    private OnItemClick onArticleClick;
    final private Activity activity;

    final protected View contentView;


    public MediumArticleUnit(@NonNull Context context, String articleId, OnItemClick onItemClick, int layoutID, Activity activity) {
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
        this.activity = activity;
        contentView = view;
        setDetails(articleId);
    }

//    public MediumArticleUnit(@NonNull Context context, String articleId, OnItemClick onItemClick) {
//        super(context);
//        // Inflate view
//        View view = inflate(getContext(), R.layout.home_news_medium_article, this);
//
//        // Get the data
//        this.r = view.getResources();
//        this.articleLayout = view.findViewById(R.id.home_news_constraintLayout);
//        this.articleImage = view.findViewById(R.id.medium_article_image);
//        this.titleTextView = view.findViewById(R.id.medium_article_name);
//        this.timeTextView = view.findViewById(R.id.medium_article_time);
//        this.onArticleClick = onItemClick;
//        contentView = view;
//        setDetails(articleId);
//    }

    public void setDetails(String articleId){
        new ArticleLoader().loadArticle(articleId, r, this);
    }


    @Override
    public void onArticleLoaded(Article article) {
        this.article = article;
        
        titleTextView.setText(article.getTitle());
        ScreenUtil.setTimeToTextView(article.getTimestamp(), timeTextView);
        if(article.getImageURLs().length != 0){
            ImageUtil.setImageToSmallView(article.getImageURLs()[0], articleImage);
        } else if(article.getVideoURLs().length != 0){
            ImageUtil.setImageToSmallView(ImageUtil.getYoutubeThumbnail(article.getVideoURLs()[0]), articleImage);
        }

        contentView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onArticleClick.onArticleClicked(article);
            }
        });
    }

    @Override
    public boolean isActivityDestroyed() {
        return activity.isDestroyed();
    }
}

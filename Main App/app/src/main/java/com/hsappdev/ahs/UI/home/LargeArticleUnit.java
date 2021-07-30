package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

public class LargeArticleUnit implements View.OnClickListener, LoadableCallback {
    private static final String TAG = "LargeArticleUnit";
    private Article article;
    final private ConstraintLayout articleLayout;
    final private ImageView articleImage;
    final private Resources r;
    final private TextView titleTextView;
    final private TextView timeTextView;
    final private TextView categoryTextView;
    final private ImageView indicatorImageView;
    private OnItemClick onArticleClick;
    final private Activity activity;

    public LargeArticleUnit(@NonNull View itemView, OnItemClick onArticleClick, Activity activity) {
        this.r = itemView.getResources();
        this.articleLayout = itemView.findViewById(R.id.home_news_constraintLayout);
        this.articleImage = itemView.findViewById(R.id.featured_article_image);
        this.titleTextView = itemView.findViewById(R.id.featured_article_name);
        this.categoryTextView = itemView.findViewById(R.id.featured_article_category);
        this.indicatorImageView = itemView.findViewById(R.id.featured_article_indicator);
        this.timeTextView = itemView.findViewById(R.id.featured_article_time);
        this.onArticleClick = onArticleClick;
        this.activity = activity;

    }

    public void setDetails(String articleID){
        articleLayout.setOnClickListener(this);
        ArticleLoaderBackend.getInstance(activity.getApplication()).getCacheObject(articleID, r, this);
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "article click");
        if(article != null)
            onArticleClick.onArticleClicked(article);
    }


    @Override
    public <T> void onLoaded(T articleN) {
        this.article = (Article) articleN;
        titleTextView.setText(article.getTitle());
        if(article.getImageURLs().length != 0) { // When there are at least one article, show first image
            ImageUtil.setImageToView(article.getImageURLs()[0], articleImage);
        } else if(article.getVideoURLs().length != 0){
            ImageUtil.setImageToSmallView(ImageUtil.getYoutubeThumbnail(article.getVideoURLs()[0]), articleImage);
        }

        ScreenUtil.setTimeToTextView(article.getTimestamp(), timeTextView);

        categoryTextView.setText(article.getCategoryDisplayName());
        categoryTextView.setTextColor(article.getCategoryDisplayColor());
        indicatorImageView.setColorFilter(article.getCategoryDisplayColor(), PorterDuff.Mode.SRC_OVER);
    }

    @Override
    public boolean isActivityDestroyed() {
        return activity.isDestroyed();
    }
}
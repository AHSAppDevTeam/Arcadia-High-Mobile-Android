package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

public class SmallArticleUnit extends MediumArticleUnit{

    private TextView timeTextView;
    private TextView categoryTextView;
    private ImageView indicatorImageView;

    public SmallArticleUnit(@NonNull Context context, String articleId, OnItemClick onItemClick) {
        super(context, articleId, onItemClick, R.layout.home_news_small_article);
    }

    @Override
    public void onArticleRetrieved() {
        super.onArticleRetrieved();
        timeTextView = contentView.findViewById(R.id.medium_article_time);
        categoryTextView = contentView.findViewById(R.id.medium_article_category);
        indicatorImageView = contentView.findViewById(R.id.medium_article_indicator);

        categoryTextView.setText(article.getCategoryDisplayName());
        categoryTextView.setTextColor(article.getCategoryDisplayColor());
        indicatorImageView.setColorFilter(article.getCategoryDisplayColor(), PorterDuff.Mode.SRC_OVER);

    }
}

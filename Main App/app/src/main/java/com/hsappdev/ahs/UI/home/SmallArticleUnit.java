package com.hsappdev.ahs.UI.home;

import android.content.Context;

import androidx.annotation.NonNull;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

public class SmallArticleUnit extends MediumArticleUnit{
    public SmallArticleUnit(@NonNull Context context, String articleId, OnItemClick onItemClick) {
        super(context, articleId, onItemClick, R.layout.home_news_small_article);
    }
}

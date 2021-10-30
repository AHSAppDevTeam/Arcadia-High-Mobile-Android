package com.hsappdev.ahs.UI.home.community;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.callbacks.ArticleLoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ScreenUtil;

public class SingleLineCommunityArticleUnit extends CardView implements ArticleLoadableCallback{

    final protected View contentView;
    protected Article article;

    private TextView articleTitle;
    private TextView articleTime;

    final private Activity activity;
    final Resources r;

    public SingleLineCommunityArticleUnit(@NonNull Context context, String articleId, OnItemClick onArticleClick, Activity activity) {
        super(context);
        View view = getViewToInflate(context, this);
        contentView = view;

        r = getResources();

        getAttributesFromView();

        setUpVisualEffects();

        this.activity = activity;

        setDetails(articleId);

        setOnClickListener(v -> {
            if(article != null) {
                onArticleClick.onArticleClicked(article);
            }
        });
    }

    private void setUpVisualEffects() {
        int p = 10;
        setPadding(p, p, p, p);
        setRadius(r.getDimension(R.dimen.padding));
        setCardElevation(10);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setForeground(r.getDrawable(outValue.resourceId, getContext().getTheme()));
        setClickable(true);
        setFocusable(true);
    }

    protected void updateUI() {
        articleTitle.setText(article.getTitle());
        ScreenUtil.setTimeToTextView(article.getTimestamp(), articleTime);
    }

    protected View getViewToInflate(Context context, ViewGroup root) {
        return inflate(context, R.layout.single_line_community_article_holder, root);
    }

    protected void getAttributesFromView() {
        articleTitle = contentView.findViewById(R.id.single_line_community_article_title);
        articleTime = contentView.findViewById(R.id.single_line_community_article_time);
    }

    @Override
    public  void onLoaded(Article articleN) {
        this.article = articleN;
        updateUI();
    }

    private void setDetails(String articleId) {
        ArticleLoaderBackend.getInstance(activity.getApplication()).getCacheObject(articleId, r, this);
    }

    @Override
    public boolean isActivityDestroyed() {
        return activity.isDestroyed();
    }
}

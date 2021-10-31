package com.hsappdev.ahs.UI.home.community;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.callbacks.ArticleLoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

public class CommunityArticleUnit extends CardView implements ArticleLoadableCallback {

    final protected View contentView;
    protected Article article;

    private TextView title;
    private TextView description;
    private TextView time;

    private ImageView image;
    final private Activity activity;
    final Resources r;

    final boolean isSmall;

    public CommunityArticleUnit(@NonNull Context context, String articleId, OnItemClick onArticleClick, Activity activity, boolean isSmall) {
        super(context);

        View view = getViewToInflate(context, this);
        contentView = view;

        r = getResources();

        this.isSmall = isSmall;

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

    protected void getAttributesFromView() {
        title = findViewById(R.id.community_article_title);
        description = findViewById(R.id.community_article_description);
        time = findViewById(R.id.community_article_time);
        image = findViewById(R.id.community_article_image);
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

    protected View getViewToInflate(Context context, ViewGroup root) {
        return inflate(context, R.layout.comunity_activity_article_unit, root);
    }

    private void setDetails(String articleId) {
        ArticleLoaderBackend.getInstance(activity.getApplication()).getCacheObject(articleId, r, this);
    }




    @Override
    public  void onLoaded(Article articleN) {
        this.article = articleN;
        updateUI();
    }

    protected void updateUI() {
        title.setText(article.getTitle());
        ScreenUtil.setTimeToTextView(article.getTimestamp(), time);
        if(!isSmall) {
            description.setVisibility(View.VISIBLE);
            ScreenUtil.setPlainHTMLStringToTextView(article.getBody(), description);
        } else {
            description.setVisibility(View.GONE);
        }
        setImageToView();
    }

    protected void setImageToView() {
        if(article.getImageURLs().length > 0) {
            ImageUtil.setImageToView(article.getImageURLs()[0], image);
        }
    }

    @Override
    public boolean isActivityDestroyed() {
        return activity.isDestroyed();
    }
}

package com.hsappdev.ahs.UI.home.community;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.cache.callbacks.ArticleLoadableCallback;
import com.hsappdev.ahs.cache_new.ArticleLoaderBackEnd;
import com.hsappdev.ahs.cache_new.DataLoaderBackEnd;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.ArticleRepository;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

public class CommunityArticleUnit extends CardView implements ArticleLoadableCallback {

    final private View contentView;
    private Article article;

    final private TextView title;
    final private TextView description;
    final private TextView time;

    final private ImageView image;
    final private AppCompatActivity activity;
    final Resources r;

    final boolean isSmall;

    public CommunityArticleUnit(@NonNull Context context, String articleId, OnItemClick onArticleClick, AppCompatActivity activity, boolean isSmall) {
        super(context);

        View view = inflate(context, R.layout.comunity_activity_article_unit, this);
        contentView = view;

        r = getResources();

        this.isSmall = isSmall;

        title = findViewById(R.id.community_article_title);
        description = findViewById(R.id.community_article_description);
        time = findViewById(R.id.community_article_time);
        image = findViewById(R.id.community_article_image);
        int p = 10;
        setPadding(p, p, p, p);
        setRadius(r.getDimension(R.dimen.padding));
        setCardElevation(10);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setForeground(r.getDrawable(outValue.resourceId, getContext().getTheme()));
        setClickable(true);
        setFocusable(true);

        this.activity = activity;

        setDetails(articleId);

        setOnClickListener(v -> {
            if(article != null) {
                onArticleClick.onArticleClicked(article);
            }
        });

    }

    private void setDetails(String articleId) {
        ArticleLoaderBackEnd loader = new ArticleLoaderBackEnd(articleId,
                getResources(),new ArticleRepository(activity.getApplication()));
        loader.getLiveData().observe(activity, new Observer<DataLoaderBackEnd.DataWithSource<Article>>() {
            @Override
            public void onChanged(DataLoaderBackEnd.DataWithSource<Article> articleDataWithSource) {
                article = articleDataWithSource.getData();
                title.setText(article.getTitle());
                ScreenUtil.setTimeToTextView(article.getTimestamp(), time);
                if(!isSmall) {
                    description.setVisibility(View.VISIBLE);
                    ScreenUtil.setPlainHTMLStringToTextView(article.getBody(), description);
                } else {
                    description.setVisibility(View.GONE);
                }
                if(article.getImageURLs().length > 0) {
                    ImageUtil.setImageToView(article.getImageURLs()[0], image);
                }
            }
        });
        /*ArticleLoaderBackend.getInstance(activity.getApplication()).getCacheObject(articleId, r, this);*/
    }




    @Override
    public  void onLoaded(Article articleN) {
        this.article = articleN;
        title.setText(article.getTitle());
        ScreenUtil.setTimeToTextView(article.getTimestamp(), time);
        if(!isSmall) {
            description.setVisibility(View.VISIBLE);
            ScreenUtil.setPlainHTMLStringToTextView(article.getBody(), description);
        } else {
            description.setVisibility(View.GONE);
        }
        if(article.getImageURLs().length > 0) {
            ImageUtil.setImageToView(article.getImageURLs()[0], image);
        }
    }

    @Override
    public boolean isActivityDestroyed() {
        return activity.isDestroyed();
    }
}

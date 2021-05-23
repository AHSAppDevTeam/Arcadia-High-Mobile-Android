package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

/**
 * A fragment that holds a single article of the medium level
 */
public class MediumLevelArticleFragment extends LinearLayout {
    private static final String TAG = "MediumLevelArticleFragment";

    // Fields
    private Article article;
    private OnItemClick onArticleClick;

    private Resources r;

    private ImageView articleImage;
    private TextView titleTextView;
    private TextView timeTextView;

    public MediumLevelArticleFragment(Article article, OnItemClick onArticleClick, Context context) {
        super(context);
        this.article = article;
        this.onArticleClick = onArticleClick;
        inflateViewAndGetElements();
    }

    private void inflateViewAndGetElements(){
        View view = inflate(getContext(), R.layout.home_news_medium_level_article_fragment, this);
        // Get all the elements
        articleImage = view.findViewById(R.id.medium_level_article_image);
        titleTextView = view.findViewById(R.id.medium_level_article_title);
        timeTextView = view.findViewById(R.id.medium_level_article_time);
        r = view.getResources();

        setDetails();
    }


    public void setDetails(){
        titleTextView.setText(article.getTitle());
        ScreenUtil.setTimeToTextView(article.getTimestamp(),timeTextView);
        // Image
        if(article.getImageURLs().length != 0) { // When there are at least one article, show first image
            ImageUtil.setImageToView(article.getImageURLs()[0], articleImage);
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "clicked");
                onArticleClick.onArticleClicked(article);
            }
        });
    }
}

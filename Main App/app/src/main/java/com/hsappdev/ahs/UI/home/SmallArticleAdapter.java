package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.annotation.NonNull;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

import java.util.List;

public class SmallArticleAdapter extends MultiArticleAdapter<SmallArticleAdapter.SmallArticleViewHolder> {
    public static final int numArticles = 2;
    private static final String TAG = "MediumArticleAdapter";

    public SmallArticleAdapter(List<String> articleIds, OnItemClick onArticleClick, Activity activity) {
        super(articleIds, onArticleClick, activity);

    }

    @NonNull
    @Override
    public SmallArticleAdapter.SmallArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SmallArticleAdapter.SmallArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_small_article_holder,
                        parent,
                        false
                ),
                onArticleClick,
                activity
        );
    }


    public static class SmallArticleViewHolder extends MultiArticleAdapter.MultiArticleViewHolder {
        private final LinearLayout linearLayout;
        private final Activity activity;

        public SmallArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick, Activity activity) {
            super(itemView, onArticleClick);
            this.linearLayout = itemView.findViewById(R.id.home_news_small_article_linear_layout);
            this.activity = activity;

        }


        public void setDetails(List<String> articlesToAdd) {
            linearLayout.removeAllViews();

            for (int i = 0; i < articlesToAdd.size(); i++) {
                String articleId = articlesToAdd.get(i);
                SmallArticleUnit smallArticleUnit = new SmallArticleUnit(itemView.getContext(), articleId, onArticleClick, activity);
                linearLayout.addView(smallArticleUnit);
                if (i % 2 == 0) {
                    // add padding
                    Space space = new Space(itemView.getContext());
                    space.setMinimumHeight(r.getDimensionPixelSize(R.dimen.padding));
                    linearLayout.addView(space);
                }
            }
        }

    }
}

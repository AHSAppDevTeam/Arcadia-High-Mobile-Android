package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

import java.util.ArrayList;
import java.util.List;

public class SmallArticleAdapter extends MultiArticleAdapter<SmallArticleAdapter.SmallArticleViewHolder>{
    private static final String TAG = "MediumArticleAdapter";


    public static final int numArticles = 2;

    public SmallArticleAdapter(List<String> articleIds, OnItemClick onArticleClick) {
        super(articleIds, onArticleClick);

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
                onArticleClick
        );
    }


    static class SmallArticleViewHolder extends MultiArticleAdapter.MultiArticleViewHolder{
        private final LinearLayout linearLayout;

        public SmallArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView, onArticleClick);
            this.linearLayout = itemView.findViewById(R.id.home_news_small_article_linear_layout);

        }


        public void setDetails(List<String> articlesToAdd){
            linearLayout.removeAllViews();

            for(int i=0; i<articlesToAdd.size(); i++){
                String articleId = articlesToAdd.get(i);
                SmallArticleUnit smallArticleUnit =  new SmallArticleUnit(itemView.getContext(), articleId, onArticleClick);
                linearLayout.addView(smallArticleUnit);

            }
        }

    }
}

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

public class MediumArticleAdapter extends MultiArticleAdapter<MediumArticleAdapter.MediumArticleViewHolder>{
    private static final String TAG = "MediumArticleAdapter";

    public MediumArticleAdapter(List<String> articleIds, OnItemClick onArticleClick) {
        super(articleIds, onArticleClick);
    }

    @NonNull
    @Override
    public MediumArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediumArticleAdapter.MediumArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_medium_article_holder,
                        parent,
                        false
                ),
                onArticleClick
        );
    }


    static public class MediumArticleViewHolder extends MultiArticleAdapter.MultiArticleViewHolder{
        private final LinearLayout linearLayoutLeft;
        private final LinearLayout linearLayoutRight;

        public MediumArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView, onArticleClick);
            this.linearLayoutLeft = itemView.findViewById(R.id.home_news_medium_article_linear_layout_left);
            this.linearLayoutRight = itemView.findViewById(R.id.home_news_medium_article_linear_layout_right);


        }


        public void setDetails(List<String> articlesToAdd){
            linearLayoutLeft.removeAllViews();
            linearLayoutRight.removeAllViews();

            for(int i=0; i<articlesToAdd.size(); i++){
                String articleId = articlesToAdd.get(i);
                MediumArticleUnit mediumArticleUnit =  new MediumArticleUnit(itemView.getContext(), articleId, onArticleClick);
                // Alternate back and forth between the left and right linear layout
                // First start with the left linear layout
                if(i % 2 == 0) {
                    linearLayoutLeft.addView(mediumArticleUnit);
                }else{
                    linearLayoutRight.addView(mediumArticleUnit);
                }
            }
        }

    }
}

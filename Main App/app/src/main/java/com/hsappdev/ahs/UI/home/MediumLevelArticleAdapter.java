package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.content.res.Resources;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;


public class MediumLevelArticleAdapter extends RecyclerView.Adapter<MediumLevelArticleAdapter.MediumLevelArticleViewHolder> {
    private final Fragment activity;
    private OnItemClick onArticleClick;
    /*
     * INTRO:
     * Articles in this class are stored as a list.
     * Using certain calculations, each pair of articles is mapped to a view holder
     * the calculations also account for the odd case in which the last viewholder only has one article
     * Each viewholder needs two articles
     * The odd/even case must also be handled (ex. it is possible for a viewholder to one have one
     * article if the total number of articles is odd)
     */
    private List<Article> articles;
    public MediumLevelArticleAdapter(List<Article> articles, OnItemClick onArticleClick, Fragment activity) {
        this.onArticleClick = onArticleClick;
        this.articles = articles;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MediumLevelArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediumLevelArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_medium_level_holder, // This view will hold two articles
                        parent,
                        false
                ),
                onArticleClick
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MediumLevelArticleViewHolder holder, int position) {
        /* We calculate the position within the articles array by reversing the calculations done in "getItemCount()"
         * We check to make sure that pos2 is less than articles.size() because pos2 might not exist (odd case)
         */
        int pos1 = (position)*2;
        int pos2 = (position)*2+1;
        List<Article> articlesToPlace = new ArrayList<>();
        if(pos2 < articles.size()){
            // even, no empty space
            articlesToPlace.add(articles.get(pos1));
            articlesToPlace.add(articles.get(pos2));

        }else{
            // odd, so one empty space (null)
            articlesToPlace.add(articles.get(pos1));
        }
        holder.setDetails(articlesToPlace);
    }

    @Override
    public int getItemCount() {
        if(articles.size()%2 == 0){
            // if even, then there are no extra view holders needed
            return articles.size()/2;
        }else{
            // if odd, one extra view holder is needed
            return articles.size()/2+1;
        }
    }

    public void clearAll() {
        articles.clear();
        notifyDataSetChanged();
    }

    public void addArticle(Article article) {

        articles.add(article);
        int insertPos = 0;
        if(articles.size()%2 == 0){
            // if it is even now, it was odd before
            insertPos = articles.size()/2;
        }else{
            // if it is odd now, it was even before
            // because it is odd now, we increment the insertPos to account for a viewholder with only 1 article
            insertPos = articles.size()/2+1;
        }
        notifyItemInserted(insertPos);
    }

    class MediumLevelArticleViewHolder extends RecyclerView.ViewHolder {
        final private Resources r;
        final private OnItemClick onArticleClick;
        final private ViewGroup holder1, holder2;
        public MediumLevelArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView);
            this.r = itemView.getResources();
            this.onArticleClick = onArticleClick;

            this.holder1 = itemView.findViewById(R.id.home_news_medium_level_holder1);
            this.holder2 = itemView.findViewById(R.id.home_news_medium_level_holder2);

        }


        /**
         * Set details for the dual-article viewholder
         */
        public void setDetails(List<Article> articles) {
            int index = 1;
            for (Article article : articles) {
                MediumLevelArticleFragment articleFragment = new MediumLevelArticleFragment(article, onArticleClick, itemView.getContext());
                if(index == 1){
                    holder1.addView(articleFragment);
                }else{
                    holder2.addView(articleFragment);
                }
                index++;
//                View articleView = View.inflate(itemView.getContext(), R.layout.home_news_medium_level_article_fragment, linearLayout);
//                TextView articleTitle = articleView.findViewById(R.id.medium_level_article_title);
//                ImageView articleImage = articleView.findViewById(R.id.medium_level_article_image);
//                articleTitle.setText(article.getTitle());
//                if(article.getImageURLs().length != 0) { // When there are at least one article, show first image
//                    ImageUtil.setImageToView(article.getImageURLs()[0], articleImage);
//                }
            }
        }
    }
}

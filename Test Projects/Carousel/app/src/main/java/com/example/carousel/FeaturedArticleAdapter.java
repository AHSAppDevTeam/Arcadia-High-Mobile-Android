package com.example.carousel;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class FeaturedArticleAdapter extends RecyclerView.Adapter<FeaturedArticleAdapter.FeaturedArticleViewHolder> {

    private List<FeaturedArticle> featuredArticles;
    private ViewPager2 viewPager2;

    public FeaturedArticleAdapter(List<FeaturedArticle> featuredArticles, ViewPager2 viewPager2) {
        this.featuredArticles = featuredArticles;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public FeaturedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeaturedArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.featured_article_slide,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedArticleViewHolder holder, int position) {
        holder.setArticleImage(featuredArticles.get(position).getArticleImage());
    }

    @Override
    public int getItemCount() {
        return featuredArticles.size();
    }

    class FeaturedArticleViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout articleLayout;
        private ImageView articleImage;

        public FeaturedArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.articleLayout = (ConstraintLayout) itemView;
            this.articleImage = articleLayout.findViewById(R.id.featured_article_image);
        }

        public void setArticleImage(int articleImage) {
            this.articleImage.setImageResource(articleImage);
        }

    }

}

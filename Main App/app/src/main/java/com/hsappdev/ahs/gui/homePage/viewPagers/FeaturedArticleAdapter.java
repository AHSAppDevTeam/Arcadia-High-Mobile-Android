package com.hsappdev.ahs.gui.homePage.viewPagers;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.List;

public class FeaturedArticleAdapter extends RecyclerView.Adapter<FeaturedArticleAdapter.FeaturedArticleViewHolder> {

    private List<Article> articles;

    public FeaturedArticleAdapter(List<Article> articles) {
        this.articles = articles;
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
        holder.setArticleImage(articles.get(position).getArticleImage());
    }

    @Override
    public int getItemCount() {
        return articles.size();
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
            // Set clip to outline programmatically since xml is not supported due to a bug with android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.articleImage.setClipToOutline(true);
            }
            this.articleImage.setImageResource(articleImage);
        }

    }

}


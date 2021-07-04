package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoader;
import com.hsappdev.ahs.cache.OnArticleLoadedCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

import java.util.List;

public class FeaturedArticleAdapter extends RecyclerView.Adapter<FeaturedArticleAdapter.FeaturedArticleViewHolder> {
    private static final String TAG = "FeaturedArticleAdapter";
    private List<String> articleIds;
    private OnItemClick onArticleClick;
    private Activity activity;

    public FeaturedArticleAdapter(List<String> articleIds, OnItemClick onArticleClick, Activity activity) {
        this.articleIds = articleIds;
        this.onArticleClick = onArticleClick;
        this.activity = activity;
    }

    // GETTERS AND SETTERS
    public List<String> getArticleIds() {
        return articleIds;
    }

    public void addArticleIds(String articleId) {
        articleIds.add(articleId);
        notifyItemInserted(articleIds.size()-1);
    }
    public void clearAll() {
        articleIds.clear();
        notifyDataSetChanged();
    }
    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeaturedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeaturedArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_featured_article,
                        parent,
                        false
                ),
                onArticleClick,
                activity
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedArticleViewHolder holder, int position) {
        holder.setDetails(articleIds.get(position));
    }

    @Override
    public int getItemCount() {
        return articleIds.size();
    }

    static class FeaturedArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnArticleLoadedCallback {
        private Article article;
        final private ConstraintLayout articleLayout;
        final private ImageView articleImage;
        final private Resources r;
        final private TextView titleTextView;
        final private TextView timeTextView;
        final private TextView categoryTextView;
        final private ImageView indicatorImageView;
        private OnItemClick onArticleClick;
        final private Activity activity;

        public FeaturedArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick, Activity activity) {
            super(itemView);
            this.r = itemView.getResources();
            this.articleLayout = itemView.findViewById(R.id.home_news_constraintLayout);
            this.articleImage = itemView.findViewById(R.id.featured_article_image);
            this.titleTextView = itemView.findViewById(R.id.featured_article_name);
            this.categoryTextView = itemView.findViewById(R.id.featured_article_category);
            this.indicatorImageView = itemView.findViewById(R.id.featured_article_indicator);
            this.timeTextView = itemView.findViewById(R.id.featured_article_time);
            this.onArticleClick = onArticleClick;
            this.activity = activity;

        }

        public void setDetails(String articleID){
            articleLayout.setOnClickListener(this);
            ArticleLoader.getInstance(activity.getApplication()).getArticle(articleID, r, this);
        }


        @Override
        public void onClick(View view) {
            //Log.d(TAG, "article click");
            if(article != null)
                onArticleClick.onArticleClicked(article);
        }

        @Override
        public void onArticleLoaded(Article article) {
            this.article = article;

            titleTextView.setText(article.getTitle());
            if(article.getImageURLs().length != 0) { // When there are at least one article, show first image
                ImageUtil.setImageToView(article.getImageURLs()[0], articleImage);
            } else if(article.getVideoURLs().length != 0){
                ImageUtil.setImageToSmallView(ImageUtil.getYoutubeThumbnail(article.getVideoURLs()[0]), articleImage);
            }

            ScreenUtil.setTimeToTextView(article.getTimestamp(), timeTextView);

            categoryTextView.setText(article.getCategoryDisplayName());
            categoryTextView.setTextColor(article.getCategoryDisplayColor());
            indicatorImageView.setColorFilter(article.getCategoryDisplayColor(), PorterDuff.Mode.SRC_OVER);
        }

        @Override
        public boolean isActivityDestroyed() {
            return activity.isDestroyed();
        }
    }


}


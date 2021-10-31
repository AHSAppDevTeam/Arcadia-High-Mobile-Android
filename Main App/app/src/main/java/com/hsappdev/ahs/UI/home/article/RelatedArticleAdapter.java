package com.hsappdev.ahs.UI.home.article;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

import java.util.List;

public class RelatedArticleAdapter extends RecyclerView.Adapter<RelatedArticleAdapter.RelatedArticleViewHolder> {
    private final List<String> articleIds;
    private final OnItemClick onArticleClick;
    private final Activity activity;

    public RelatedArticleAdapter(List<String> articleIds, OnItemClick onArticleClick, Activity activity) {
        super();
        this.articleIds = articleIds;
        this.onArticleClick = onArticleClick;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RelatedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RelatedArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_small_article,
                        parent,
                        false
                ),
                onArticleClick
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedArticleViewHolder holder, int position) {
        holder.setDetails(articleIds.get(position));
    }

    @Override
    public int getItemCount() {
        return articleIds.size();
    }


    public class RelatedArticleViewHolder extends RecyclerView.ViewHolder implements LoadableCallback<Article>, View.OnClickListener {
        private final OnItemClick onItemClick;
        private final ImageView articleImage;
        private final TextView titleTextView;
        private final TextView timeTextView;
        private final TextView categoryTextView;
        private final ImageView indicatorImageView;
        private Article article;

        public RelatedArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView);

            this.onItemClick = onArticleClick;

            this.articleImage = itemView.findViewById(R.id.medium_article_image);
            this.titleTextView = itemView.findViewById(R.id.medium_article_name);
            this.timeTextView = itemView.findViewById(R.id.medium_article_time);
            this.categoryTextView = itemView.findViewById(R.id.medium_article_category);
            this.indicatorImageView = itemView.findViewById(R.id.medium_article_indicator);

            itemView.setOnClickListener(this);
        }


        public void setDetails(String articleId) {
            ArticleLoaderBackend.getInstance(activity.getApplication()).getCacheObject(articleId, activity.getResources(), this);
        }

        @Override
        public void onLoaded(Article article) {
            this.article = article;
            titleTextView.setText(article.getTitle());
            if (article.getImageURLs().length != 0) { // When there are at least one article, show first image
                ImageUtil.setImageToView(article.getImageURLs()[0], articleImage);
            } else if (article.getVideoURLs().length != 0) {
                ImageUtil.setImageToSmallView(ImageUtil.getYoutubeThumbnail(article.getVideoURLs()[0]), articleImage);
            }

            ScreenUtil.setTimeToTextView(article.getTimestamp(), timeTextView);

            categoryTextView.setText(article.getCategoryDisplayName());
            categoryTextView.setTextColor(article.getCategoryDisplayColor());
            indicatorImageView.setColorFilter(article.getCategoryDisplayColor(), PorterDuff.Mode.SRC_OVER);
            Log.d("relatedArticle", "onLoaded: " + article.getTitle() + " this: " + article);
        }

        @Override
        public boolean isActivityDestroyed() {
            return activity.isDestroyed();
        }

        @Override
        public void onClick(View v) {
            if (article != null) {
                onItemClick.onArticleClicked(article);
            }
        }
    }
}



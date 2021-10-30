package com.hsappdev.ahs.UI.home.article;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.home.MediumArticleAdapter;
import com.hsappdev.ahs.UI.home.MultiArticleAdapter;
import com.hsappdev.ahs.UI.home.SmallArticleAdapter;
import com.hsappdev.ahs.UI.saved.SavedRecyclerAdapter;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.List;

public class RelatedArticleAdapter extends RecyclerView.Adapter<RelatedArticleAdapter.RelatedArticleViewHolder> {
    private List<String> articleIds;
    private OnItemClick onArticleClick;
    private Activity activity;
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


    public class RelatedArticleViewHolder extends RecyclerView.ViewHolder implements LoadableCallback<Article> {
        private final TextView title;
        public RelatedArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView);

            title = itemView.findViewById(R.id.medium_article_name);
        }


        public void setDetails(String articleId) {
            ArticleLoaderBackend.getInstance(activity.getApplication()).getCacheObject(articleId, activity.getResources(), this);
        }

        @Override
        public void onLoaded(Article article) {
            title.setText(article.getTitle());
            Log.d("relatedArticle", "onLoaded: " + article.getTitle());
        }

        @Override
        public boolean isActivityDestroyed() {
            return activity.isDestroyed();
        }
    }
}



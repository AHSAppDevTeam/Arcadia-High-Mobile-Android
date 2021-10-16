package com.hsappdev.ahs.UI.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.ArrayList;
import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationArticleViewHolder> {

    private List<Article> savedArticleList = new ArrayList<>();
    private SortedList<Article> articleSortedList;
    private final OnItemClick onArticleClick;
    private int sortMode = 0;

    public NotificationRecyclerAdapter(OnItemClick onArticleClick) {
        this.onArticleClick = onArticleClick;
        articleSortedList = new SortedList<>(Article.class, new SortedList.Callback<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                if(sortMode == 0) {
                    return (int) (o2.getTimestamp() - o1.getTimestamp());
                } else if(sortMode == 1) {
                    return (int) -(o2.getTimestamp() - o1.getTimestamp());
                }
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Article oldItem, Article newItem) {
                return oldItem.getArticleID().equals(newItem.getArticleID());
            }

            @Override
            public boolean areItemsTheSame(Article item1, Article item2) {
                return item1.getArticleID().equals(item2.getArticleID());
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    public void onSortModeChanged(int sort) {
        sortMode = sort;
        articleSortedList.replaceAll(savedArticleList);

    }

    @NonNull
    @Override
    public NotificationRecyclerAdapter.NotificationArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_article_holder, parent, false);
        return new NotificationRecyclerAdapter.NotificationArticleViewHolder(view);
    }

    public void replaceAll(List<Article> articles) {
        savedArticleList.clear();
        savedArticleList.addAll(articles);
        articleSortedList.replaceAll(savedArticleList);
    }

    public void addArticle(Article article){
        savedArticleList.add(article);
        articleSortedList.add(article);
    }

    public void addArticles(List<Article> articleList){
        savedArticleList.addAll(articleList);
        articleSortedList.addAll(articleList);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerAdapter.NotificationArticleViewHolder holder, int position) {
        if(position < articleSortedList.size()) {
            holder.setDetails(articleSortedList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return savedArticleList.size();
    }

    public class NotificationArticleViewHolder extends RecyclerView.ViewHolder{

        public NotificationArticleViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void setDetails(Article article){

        }
    }


}

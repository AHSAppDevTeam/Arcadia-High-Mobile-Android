package com.hsappdev.ahs.UI.saved;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.home.NewsRecyclerAdapter;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class SavedRecyclerAdapter extends RecyclerView.Adapter<SavedRecyclerAdapter.SavedArticleViewHolder>{

    private List<Article> savedArticleList = new ArrayList<>();
    private SortedList<Article> articleSortedList;
    private final OnItemClick onArticleClick;
    private int sortMode = 0;

    public SavedRecyclerAdapter(OnItemClick onArticleClick) {
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
                return item1.equals(item2);
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
        List<Article> tempArticleHolder = new ArrayList<>(savedArticleList);
        clearAll();
        addArticles(tempArticleHolder);
    }

    @NonNull
    @Override
    public SavedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_article_holder, parent, false);
        return new SavedRecyclerAdapter.SavedArticleViewHolder(view);
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
    public void onBindViewHolder(@NonNull SavedArticleViewHolder holder, int position) {
        holder.setDetails(articleSortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return savedArticleList.size();
    }

    public void clearAll() {
        savedArticleList.clear();
        articleSortedList.clear();
    }

    public class SavedArticleViewHolder extends RecyclerView.ViewHolder{

        private final TextView title;
        private final TextView category;
        private final TextView timeStamp;
        private final ImageView indicator;

        public SavedArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.saved_article_title);
            this.category = itemView.findViewById(R.id.saved_article_category);
            this.indicator = itemView.findViewById(R.id.saved_article_indicator);
            this.timeStamp = itemView.findViewById(R.id.saved_article_time);
        }

        public void setDetails(Article article){
            title.setText(article.getTitle());
            Helper.setBoldRegularText(category, article.getCategoryDisplayName(), " Section");
            category.setTextColor(article.getCategoryDisplayColor());
            indicator.setColorFilter(article.getCategoryDisplayColor());
            ScreenUtil.setTimeToTextView(article.getTimestamp(), timeStamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArticleClick.onArticleClicked(article);
                }
            });
        }
    }
}

package com.hsappdev.ahs.UI.bulletin;

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
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.util.ScreenUtil;

public class ComingUpRecyclerAdapter extends RecyclerView.Adapter<ComingUpRecyclerAdapter.ComingUpArticleViewHolder> {
    private final OnItemClick onArticleClick;
    private SortedList<Article> articleSortedList = new SortedList<Article>(Article.class, new SortedList.Callback<Article>() {
        @Override
        public int compare(Article o1, Article o2) {
            return (int) (o2.getTimestamp() - o1.getTimestamp());
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Article oldItem, Article newItem) {
            return oldItem.equals(newItem);
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

        @Override
        public void onChanged(int position, int count, Object payload) {
            notifyItemRangeChanged(position, count, payload);
        }
    });

    public ComingUpRecyclerAdapter(OnItemClick onArticleClick) {
        this.onArticleClick = onArticleClick;
    }


    @NonNull
    @Override
    public ComingUpArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_article_holder, parent, false);
        return new ComingUpRecyclerAdapter.ComingUpArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComingUpArticleViewHolder holder, int position) {
        holder.setDetails(articleSortedList.get(position), onArticleClick);
    }

    @Override
    public int getItemCount() {
        return articleSortedList.size();
    }

    public class ComingUpArticleViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView category;
        private final TextView timeStamp;
        private final ImageView indicator;

        public ComingUpArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.saved_article_title);
            this.category = itemView.findViewById(R.id.notification_article_category);
            this.indicator = itemView.findViewById(R.id.notification_article_indicator);
            this.timeStamp = itemView.findViewById(R.id.notification_article_time);
        }

        public void setDetails(Article article, OnItemClick onArticleClick){
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

    public SortedList<Article> getArticleSortedList() {
        return articleSortedList;
    }

    public void setArticleSortedList(SortedList<Article> articleSortedList) {
        this.articleSortedList = articleSortedList;
    }
}

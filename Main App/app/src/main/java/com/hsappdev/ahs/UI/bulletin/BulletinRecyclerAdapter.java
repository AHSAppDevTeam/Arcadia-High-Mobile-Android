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

public class BulletinRecyclerAdapter extends RecyclerView.Adapter<BulletinRecyclerAdapter.BulletinArticleViewHolder> {
    private final OnItemClick onArticleClick;
    private final boolean isComingUpArticles;

    private final int UP_COMING = 1, DEFAULT = 0;


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

    public BulletinRecyclerAdapter(OnItemClick onArticleClick, boolean isComingUpArticles) {
        this.onArticleClick = onArticleClick;
        this.isComingUpArticles = isComingUpArticles;
    }


    @NonNull
    @Override
    public BulletinArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == DEFAULT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bulletin_defualt_article_holder, parent, false);
            return new BulletinRecyclerAdapter.BulletinArticleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bulletin_up_coming_article_holder, parent, false);
            return new BulletinRecyclerAdapter.ComingUpArticleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinArticleViewHolder holder, int position) {
        holder.setDetails(articleSortedList.get(position), onArticleClick);
    }

    @Override
    public int getItemCount() {
        return articleSortedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(isComingUpArticles){
            return UP_COMING;
        }
        return DEFAULT;
    }

    public class ComingUpArticleViewHolder extends BulletinArticleViewHolder {
        private final TextView number;

        public ComingUpArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.bulletin_article_up_coming_number);
        }

        @Override
        public void setDetails(Article article, OnItemClick onArticleClick) {
            super.setDetails(article, onArticleClick);
            number.setText(Integer.toString(getAdapterPosition()+1));
        }
    }

    public class BulletinArticleViewHolder extends RecyclerView.ViewHolder{
        protected final TextView title;
        protected final TextView category;
        protected final TextView timeStamp;
        protected final ImageView indicator;

        public BulletinArticleViewHolder(@NonNull View itemView) {
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
            if(indicator != null) {
                indicator.setColorFilter(article.getCategoryDisplayColor());
            }
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

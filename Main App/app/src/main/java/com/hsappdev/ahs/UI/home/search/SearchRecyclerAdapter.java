package com.hsappdev.ahs.UI.home.search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.saved.SavedRecyclerAdapter;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchArticleViewHolder> {
    private List<Article> searchArticleList = new ArrayList<>();
    private List<Article> filteredArticleList = new ArrayList<>();
    private OnItemClick onItemClick;

    private static final String TAG = "SearchRecyclerAdapter";

    public SearchRecyclerAdapter(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public SearchArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_dialog_result, parent, false);
        return new SearchRecyclerAdapter.SearchArticleViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchArticleViewHolder holder, int position) {
        if(position < filteredArticleList.size()) {
            holder.setDetails(filteredArticleList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return filteredArticleList.size();
    }

    public void setArticles(List<Article> articleList){
        searchArticleList.clear();
        searchArticleList.addAll(articleList);
        notifyDataSetChanged();
    }

    public void onUpdate(String query) {
        query = query.toLowerCase();
        if(query.isEmpty()){
            filteredArticleList.clear();
            notifyDataSetChanged();
            return;
        }
        List<Article> tempList = new ArrayList<>();
        // Cannot use removeIf syntax, not supported on all platforms
        for(Article a : searchArticleList){
            if(a.getTitle().toLowerCase().contains(query)){ // Don't mind letter case
                tempList.add(a);
            }
        }
        filteredArticleList = tempList;
        Log.d(TAG, "onUpdate: " + tempList.size());
        notifyDataSetChanged();
    }

    class SearchArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView searchResult;
        private final OnItemClick onItemClick;
        private Article article;
        public SearchArticleViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            searchResult = itemView.findViewById(R.id.search_dialog_result_textView);
            this.onItemClick = onItemClick;

            searchResult.setOnClickListener(this);
        }

        public void setDetails(Article article) {
            this.article = article;
            searchResult.setText(article.getTitle());
        }

        @Override
        public void onClick(View v) {
            if(onItemClick != null && article != null){
                onItemClick.onArticleClicked(article);
            }
        }
    }
}

package com.hsappdev.ahs.UI.home.search;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.AboutUsActivity;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchArticleViewHolder> {
    private static final String TAG = "SearchRecyclerAdapter";
    private final List<Article> searchArticleList = new ArrayList<>();
    private List<Article> filteredArticleList = new ArrayList<>();
    private final OnItemClick onItemClick;
    private final Activity activity;

    public SearchRecyclerAdapter(OnItemClick onItemClick, Activity activity) {
        this.onItemClick = onItemClick;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SearchArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_dialog_result, parent, false);
        return new SearchRecyclerAdapter.SearchArticleViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchArticleViewHolder holder, int position) {
        if (position < filteredArticleList.size()) {
            holder.setDetails(filteredArticleList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return filteredArticleList.size();
    }

    public void setArticles(List<Article> articleList) {
        searchArticleList.clear();
        searchArticleList.addAll(articleList);
        notifyDataSetChanged();
    }

    public void onUpdate(String query) {
        query = query.toLowerCase();
        if (query.isEmpty()) {
            filteredArticleList.clear();
            notifyDataSetChanged();
            return;
        } else if (query.equals(activity.getResources().getString(R.string.search_magic_query))) {
            activity.startActivity(new Intent(activity.getApplicationContext(), AboutUsActivity.class));
        }
        List<Article> tempList = new ArrayList<>();
        String[] qWords = query.toLowerCase().split(" ");
        // Cannot use removeIf syntax, not supported on all platforms
        articleSearchLoop:
        for (Article a : searchArticleList) {
            String[] words = a.getTitle().toLowerCase().split(" ");
            for (String word : words) {
                for (String qWord : qWords) {
                    // Calculate distance
                    int dist;
                    if (word.length() < qWord.length()) {
                        dist = Helper.distance(word, qWord);
                    } else {
                        dist = Helper.distance(word.substring(0, qWord.length() - 1), qWord);
                    }
                    if (dist < qWord.length() / 2) {
                        tempList.add(a);
                        continue articleSearchLoop;
                    }

                    // check if contains the word
                    if (word.contains(qWord) && (qWord.length() > 3 || qWords.length == 1)) {
                        tempList.add(a);
                        continue articleSearchLoop;
                    }
                }
            }
        }
//        for(Article a : searchArticleList){
//            String[] words = a.getTitle().toLowerCase().split(" ");
//            for(String word : words) {
//                if (query.contains(word)) { // Don't mind letter case
//                    tempList.add(a);
//                    break articleSearchLoop;
//                }
//            }
//        }
        filteredArticleList = tempList;
        Log.d(TAG, "onUpdate: " + tempList.size());
        notifyDataSetChanged();
    }

    class SearchArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            if (onItemClick != null && article != null) {
                onItemClick.onArticleClicked(article);
            }
        }
    }
}

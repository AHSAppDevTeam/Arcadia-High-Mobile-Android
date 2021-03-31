package com.hsappdev.ahs.UI.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.ArrayList;
import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter {
    //List<List<Article>> articlesList = new ArrayList<>();

    ArrayList<String> categoryTitles;
    public NewsRecyclerAdapter(ArrayList<String> categoryTitles) {
        this.categoryTitles = categoryTitles;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_news_section, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FeaturedViewHolder extends RecyclerView.ViewHolder{

        private ViewPager2 homeNews;
        public void setDetails(List<Article> articles){
            FeaturedArticleAdapter featuredArticleAdapter = new FeaturedArticleAdapter(articles);
            homeNews.setAdapter(featuredArticleAdapter);
        }

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            homeNews = itemView.findViewById(R.id.home_featured_carousel);
        }
    }
}

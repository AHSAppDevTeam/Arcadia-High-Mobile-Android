package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        FeaturedViewHolder featuredHolder = (FeaturedViewHolder) holder;
        featuredHolder.setDetails(categoryTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryTitles.size();
    }

    class FeaturedViewHolder extends RecyclerView.ViewHolder{

        private ViewPager2 homeNews;
        private Resources resources;
        public void setDetails(String categoryTitle){
            List<String> articles  = new ArrayList<>();
            Resources r = homeNews.getContext().getResources();
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(resources.getString(R.string.database_categories_ref))
                    .child(categoryTitle)
                    .child(resources.getString(R.string.database_categories_articleIds_ref));
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot articleId : snapshot.getChildren()){
                        articles.add(articleId.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            FeaturedArticleAdapter featuredArticleAdapter = new FeaturedArticleAdapter(articles);
            homeNews.setAdapter(featuredArticleAdapter);
        }

        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);
            resources = itemView.getContext().getResources();
            homeNews = itemView.findViewById(R.id.home_featured_carousel);
        }
    }
}

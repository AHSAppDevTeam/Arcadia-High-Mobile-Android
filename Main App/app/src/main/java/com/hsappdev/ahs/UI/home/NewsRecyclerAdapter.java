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

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.FeaturedViewHolder> {
    //List<List<Article>> articlesList = new ArrayList<>();

    ArrayList<String> categoryTitles;
    public NewsRecyclerAdapter(ArrayList<String> categoryTitles) {
        this.categoryTitles = categoryTitles;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_news_section, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        holder.setDetails(categoryTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryTitles.size();
    }

    class FeaturedViewHolder extends RecyclerView.ViewHolder{

        private ViewPager2 homeNews;
        private Resources resources;
        public void setDetails(String categoryTitle){

            Resources r = homeNews.getContext().getResources();
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(resources.getString(R.string.database_categories_ref))
                    .child(categoryTitle)
                    .child(resources.getString(R.string.database_categories_articleIds_ref));
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> articles  = new ArrayList<>();
                    for(DataSnapshot articleId : snapshot.getChildren()){
                        articles.add(articleId.getValue(String.class));
                    }
                    if(homeNews.getAdapter() == null) {
                        FeaturedArticleAdapter featuredArticleAdapter = new FeaturedArticleAdapter(articles);
                        homeNews.setAdapter(featuredArticleAdapter);
                    }else{
                        FeaturedArticleAdapter adapter = ((FeaturedArticleAdapter)homeNews.getAdapter());
                        adapter.setArticleIds(articles);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);
            resources = itemView.getContext().getResources();
            homeNews = itemView.findViewById(R.id.home_featured_carousel);
        }
    }
}

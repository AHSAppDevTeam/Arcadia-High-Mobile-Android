package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.FeaturedViewHolder> {
    //List<List<Article>> articlesList = new ArrayList<>();
    private static final String TAG = "NewsRecyclerAdapter";
    private ArrayList<String> categoryIDs;
    private OnItemClick onArticleClick;

    public NewsRecyclerAdapter(ArrayList<String> categoryTitles, OnItemClick onArticleClick) {
        this.categoryIDs = categoryTitles;
        this.onArticleClick = onArticleClick;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_news_section, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        //Log.d(TAG, "holder bind at position " + position +"\tcategory: " + categoryIDs.get(position));
        ((ViewGroup) holder.itemView).setClipChildren(false);
        ((ViewGroup) holder.itemView).setClipToPadding(false);
        holder.setDetails(categoryIDs.get(position), onArticleClick);
    }

    @Override
    public int getItemCount() {
        return categoryIDs.size();
    }

    public void addCategoryIDs(String ID) {
        categoryIDs.add(ID);
        notifyItemInserted(categoryIDs.size()-1);
    }

    public void addCategoryIDs(ArrayList<String> IDs) {
        int oldpos = IDs.size();
        this.categoryIDs.addAll(IDs);
        notifyItemRangeInserted(oldpos,this.categoryIDs.size()-1);
    }

    public void clearAll() {
        categoryIDs.clear();
        notifyDataSetChanged();
    }



    static class FeaturedViewHolder extends RecyclerView.ViewHolder{
        private final TextView sectionTitle;
        private final ViewPager2 featuredPager;
        private final ViewPager2 mediumPager;
        private final Resources r;

        public void setDetails(String categoryTitle, OnItemClick onArticleClick){
            setUpPager();
            FeaturedArticleAdapter featuredArticleAdapter = new FeaturedArticleAdapter(new ArrayList<String>(), onArticleClick);
            MediumArticleAdapter mediumArticleAdapter = new MediumArticleAdapter(new ArrayList<String>(), onArticleClick);

            featuredPager.setAdapter(featuredArticleAdapter);
            mediumPager.setAdapter(mediumArticleAdapter);

            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(r.getString(R.string.db_categories))
                    .child(categoryTitle);
            boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    featuredArticleAdapter.clearAll();
                    mediumArticleAdapter.clearAll();

                    for(DataSnapshot articleId : snapshot.child(r.getString(R.string.db_categories_articleIds)).getChildren()){
                        featuredArticleAdapter.addArticleIds(articleId.getValue(String.class));
                        mediumArticleAdapter.addArticleId(articleId.getValue(String.class));
                        //articles.add();
                    }
                    String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);
                    int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));

                    // set section title
                    String regularText = " News";
                    Helper.setBoldRegularText(sectionTitle, title, regularText);
                    sectionTitle.setTextColor(color);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        public void setUpPager(){
            featuredPager.setOffscreenPageLimit(3);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            //margin determines distance between two pages
            //adjust left/right padding of viewpager2 to determine distance between left and right edges and current page
            //Log.d(TAG, "Dimen value: " + r.getDimension(R.dimen.padding));
            compositePageTransformer.addTransformer(new MarginPageTransformer((int) dp_to_px(2))); //note: conversion between dp and pixel, apply later
            compositePageTransformer.addTransformer(new ScaleAndFadeTransformer());
            featuredPager.setPageTransformer(compositePageTransformer);
        }

        public float dp_to_px(float dp) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, r.getDisplayMetrics());
        }

        public FeaturedViewHolder(@NonNull View itemView){
            super(itemView);
            r = itemView.getContext().getResources();

            featuredPager = itemView.findViewById(R.id.home_featured_carousel);
            mediumPager = itemView.findViewById(R.id.home_medium_carousel);
            sectionTitle = itemView.findViewById(R.id.home_news_sectionTitle);
        }
    }
}

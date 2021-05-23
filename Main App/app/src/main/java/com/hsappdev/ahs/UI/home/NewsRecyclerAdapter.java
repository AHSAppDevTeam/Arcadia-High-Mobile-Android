package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.CategoryViewHolder> {
    //List<List<Article>> articlesList = new ArrayList<>();
    private static final String TAG = "NewsRecyclerAdapter";
    private ArrayList<String> categoryIDs;
    private OnItemClick onArticleClick;
    private Fragment activity;

    public NewsRecyclerAdapter(ArrayList<String> categoryTitles, OnItemClick onArticleClick, Fragment activity) {
        this.categoryIDs = categoryTitles;
        this.onArticleClick = onArticleClick;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_news_section, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
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

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        private final TextView sectionTitle;
        private final ViewPager2 featuredPager;
        private final ViewPager2 mediumLevelPager;
        private final Resources r;


        public void setDetails(String categoryTitle, OnItemClick onArticleClick){
            setUpPager();
            FeaturedArticleAdapter featuredArticleAdapter = new FeaturedArticleAdapter(new ArrayList<Article>(), onArticleClick);
            featuredPager.setAdapter(featuredArticleAdapter);
            MediumLevelArticleAdapter mediumLevelArticleAdapter = new MediumLevelArticleAdapter(new ArrayList<Article>(), onArticleClick, activity);
            mediumLevelPager.setAdapter(mediumLevelArticleAdapter);

            setUpCategory(categoryTitle, featuredArticleAdapter, mediumLevelArticleAdapter);

        }

        public void setUpCategory(String categoryTitle, FeaturedArticleAdapter featuredArticleAdapter, MediumLevelArticleAdapter mediumLevelArticleAdapter){
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(r.getString(R.string.db_categories))
                    .child(categoryTitle);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    featuredArticleAdapter.clearAll();
                    mediumLevelArticleAdapter.clearAll();
                    for(DataSnapshot articleId : snapshot.child(r.getString(R.string.db_categories_articleIds)).getChildren()){
                        String stringID = articleId.getValue(String.class);
                        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                                .child(r.getString(R.string.db_articles))
                                .child(stringID);

                        ref.addValueEventListener(new ValueEventListener() {
                            boolean firstTime = true;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(firstTime) {
                                    firstTime = false;
                                }else{
                                    setUpCategory(categoryTitle, featuredArticleAdapter, mediumLevelArticleAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                                String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                                String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                                String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
                                ArrayList<String> imageURLs = new ArrayList<>();
                                for (DataSnapshot imageURL : snapshot.child(r.getString(R.string.db_articles_imageURLs)).getChildren()) {
                                    imageURLs.add(imageURL.getValue(String.class));
                                }
                                boolean featured = snapshot.child("featured").getValue(boolean.class);
                                long timestamp = 1;
                                // Try catch is a quick fix until Xing fixes the string timestamp problem
                                try {
                                    timestamp = snapshot.child(r.getString(R.string.db_articles_timestamp)).getValue(long.class);
                                } catch (Exception e) {

                                }

                                Article article = new Article(stringID, author, title, body, category, imageURLs.toArray(new String[0]), featured, timestamp);

                                /*
                                 * ARTICLE SORTING JUNCTION
                                 * from here, each article is sorted into their respective viewpagers
                                 */
                                if(category.equals("General_Info")){
                                    if(article.isFeatured()) {
                                        featuredArticleAdapter.addArticle(article);
                                    }else{
                                        // Add the article to a view pager of "lower class"
                                        mediumLevelArticleAdapter.addArticle(article);
                                    }
                                }else{
                                    // Limit featured articles to 1
                                    if(article.isFeatured() && featuredArticleAdapter.getArticleIds().size() == 0) {
                                        featuredArticleAdapter.addArticle(article);
                                    }else{
                                        // Add the article to a view pager of "lower class"
                                        Log.d("tag", "new1");

                                        mediumLevelArticleAdapter.addArticle(article);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });

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
            // FeaturedPager
            featuredPager.setOffscreenPageLimit(3);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            //margin determines distance between two pages
            //adjust left/right padding of viewpager2 to determine distance between left and right edges and current page
            compositePageTransformer.addTransformer(new MarginPageTransformer((int) dp_to_px(2))); //note: conversion between dp and pixel, apply later
            compositePageTransformer.addTransformer(new ScaleAndFadeTransformer());
            featuredPager.setPageTransformer(compositePageTransformer);

            // MediumLevelPager
            mediumLevelPager.setOffscreenPageLimit(3);
            //mediumLevelPager.setPageTransformer(compositePageTransformer);
        }

        public float dp_to_px(float dp) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, r.getDisplayMetrics());
        }

        public CategoryViewHolder(@NonNull View itemView){
            super(itemView);
            r = itemView.getContext().getResources();

            featuredPager = itemView.findViewById(R.id.home_featured_carousel);
            mediumLevelPager = itemView.findViewById(R.id.home_medium_carousel);
            sectionTitle = itemView.findViewById(R.id.home_news_sectionTitle);
        }
    }
}

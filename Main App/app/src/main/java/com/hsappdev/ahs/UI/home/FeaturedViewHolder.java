package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.CategoryLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.cache.callbacks.ArticleLoadableCallback;
import com.hsappdev.ahs.cache.callbacks.CategoryLoadableCallback;
import com.hsappdev.ahs.cache_new.CategoryLoaderBackEnd;
import com.hsappdev.ahs.cache_new.DataLoaderBackEnd;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.localdb.CategoryRepository;
import com.hsappdev.ahs.util.Helper;

import java.util.ArrayList;

public class FeaturedViewHolder extends RecyclerView.ViewHolder implements CategoryLoadableCallback {
    private final TextView sectionTitle;
    private final ViewPager2 featuredPager;
    private final TabLayout featuredTabLayout;
    private FeaturedArticleAdapter featuredArticleAdapter;
    private final AppCompatActivity activity;
    private final Resources r;


    public FeaturedViewHolder(@NonNull View itemView, AppCompatActivity activity) {
        super(itemView);
        featuredPager = itemView.findViewById(R.id.home_featured_carousel);
        featuredTabLayout = itemView.findViewById(R.id.featured_tab_layout);
        sectionTitle = itemView.findViewById(R.id.home_news_sectionTitle);
        this.activity = activity;
        r = itemView.getResources();
    }

    public void setDetails(String categoryTitle, OnItemClick onArticleClick) {
        setUpPager();
        featuredArticleAdapter = new FeaturedArticleAdapter(new ArrayList<String>(), onArticleClick, activity);
        featuredPager.setAdapter(featuredArticleAdapter);

        CategoryLoaderBackEnd loader = new CategoryLoaderBackEnd(categoryTitle, r, new CategoryRepository(activity.getApplication()));
        loader.getLiveData().observe(activity, new Observer<DataLoaderBackEnd.DataWithSource<Category>>() {
            @Override
            public void onChanged(DataLoaderBackEnd.DataWithSource<Category> categoryDataWithSource) {
                Category category = categoryDataWithSource.getData();
                featuredArticleAdapter.clearAll();
                /*
                 * Structure
                 * > single carousel for all articles
                 * */
                featuredArticleAdapter.setArticleIds(category.getArticleIds());

                String regularText = " News";
                Helper.setBoldRegularText(sectionTitle, category.getTitle(), regularText);
                sectionTitle.setTextColor(category.getColor());

                // Set up tab layout
                if (featuredArticleAdapter.getItemCount() > 1) {
                    featuredTabLayout.setVisibility(View.VISIBLE);
                    TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(featuredTabLayout, featuredPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        }
                    });
                    tabLayoutMediator.attach();
                } else {
                    featuredTabLayout.setVisibility(View.GONE);
                }
            }
        });

        //CategoryLoader.getInstance().getCategory(categoryTitle, r, this);
        //CategoryLoaderBackend.getInstance(activity.getApplication()).getCacheObject(categoryTitle, r, this);
    }

    public void setUpPager(){

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        //margin determines distance between two pages
        //adjust left/right padding of viewpager2 to determine distance between left and right edges and current page
        //Log.d(TAG, "Dimen value: " + r.getDimension(R.dimen.padding));
        compositePageTransformer.addTransformer(new MarginPageTransformer((int) dp_to_px(2))); //note: conversion between dp and pixel, apply later
        compositePageTransformer.addTransformer(new ScaleAndFadeTransformer(false));
        featuredPager.setPageTransformer(compositePageTransformer);

        featuredPager.setOffscreenPageLimit(3);
    }

    public float dp_to_px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

//    @Override
//    public void onCategoryLoaded(Category category) {
//        featuredArticleAdapter.clearAll();
//        /*
//         * Structure
//         * > single carousel for all articles
//         * */
//        featuredArticleAdapter.setArticleIds(category.getArticleIds());
//
//        String regularText = " News";
//        Helper.setBoldRegularText(sectionTitle, category.getTitle(), regularText);
//        sectionTitle.setTextColor(category.getColor());
//
//        // Set up tab layout
//        if (featuredArticleAdapter.getItemCount() > 1) {
//            featuredTabLayout.setVisibility(View.VISIBLE);
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(featuredTabLayout, featuredPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
//                @Override
//                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                }
//            });
//            tabLayoutMediator.attach();
//        } else {
//            featuredTabLayout.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onLoaded(Category category) {
        featuredArticleAdapter.clearAll();
        /*
         * Structure
         * > single carousel for all articles
         * */
        featuredArticleAdapter.setArticleIds(category.getArticleIds());

        String regularText = " News";
        Helper.setBoldRegularText(sectionTitle, category.getTitle(), regularText);
        sectionTitle.setTextColor(category.getColor());

        // Set up tab layout
        if (featuredArticleAdapter.getItemCount() > 1) {
            featuredTabLayout.setVisibility(View.VISIBLE);
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(featuredTabLayout, featuredPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                }
            });
            tabLayoutMediator.attach();
        } else {
            featuredTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isActivityDestroyed() {
        return activity.isDestroyed();
    }
}

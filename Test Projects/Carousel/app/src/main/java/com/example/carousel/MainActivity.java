package com.example.carousel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    List<FeaturedArticle> featuredArticleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.carousel);

        featuredArticleList = new ArrayList<>();
        // TODO: Fix article id and image
        featuredArticleList.add(new FeaturedArticle(UUID.randomUUID().toString(), R.drawable.test_image));
        featuredArticleList.add(new FeaturedArticle(UUID.randomUUID().toString(), R.drawable.test_image1));
        featuredArticleList.add(new FeaturedArticle(UUID.randomUUID().toString(), R.drawable.test_image));
        featuredArticleList.add(new FeaturedArticle(UUID.randomUUID().toString(), R.drawable.test_image1));

        viewPager2.setAdapter(new FeaturedArticleAdapter(featuredArticleList, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);

        // overscroll effect thing
        //viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        //margin determines distance between two pages
        //adjust left/right padding of viewpager2 to determine distance between left and right edges and current page
        compositePageTransformer.addTransformer(new MarginPageTransformer((int) dp_to_px(0))); //note: conversion between dp and pixel, apply later
        compositePageTransformer.addTransformer(new ScaleTransformer());
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    public float dp_to_px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

    class ScaleTransformer implements ViewPager2.PageTransformer {

        @Override
        public void transformPage(@NonNull View page, float position) {
            float curentPos = 1 - Math.abs(position);
            float maxScale = 1f;
            float minScale = 0.75f;
            float diff = maxScale-minScale;
            // Set Pivot point to center of imageView
            ImageView articleImage = page.findViewById(R.id.featured_article_image);
            int centerX = articleImage.getWidth()/2;
            int centerY = articleImage.getHeight()/2;
            page.setPivotX(centerX);
            page.setPivotY(centerY);
            // Set scale in between min and max
            page.setScaleY(minScale + diff*curentPos);
            // Fade text
            page.findViewById(R.id.featured_article_stats).setAlpha((float) Math.pow(curentPos,4));
        }
    }

    
}
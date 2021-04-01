package com.hsappdev.ahs.UI.home;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.hsappdev.ahs.BottomNavigationCallback;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private BottomNavigationCallback bottomNavigationViewAdapter;

    // For Featured Articles
    ViewPager2 featuredArticleViewPager;
    List<Article> articleList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomNavigationViewAdapter = (BottomNavigationCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    private boolean is_nav_bar_up = true;
    private int news_tab_selected = 0; // 0 for home, 1 for community; int allows for potentially more options in the future
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        Fragment homeNewsFragment = new HomeNewsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.home_news_fragment_holder, homeNewsFragment)
                .commit();


        NestedScrollView scrollView = view.findViewById(R.id.home_scrollView);
        final float scrollAnimBuffer = 5;
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            float y = 0;
            @Override
            public void onScrollChanged() {
                if(scrollView.getScrollY() > y + scrollAnimBuffer) // scroll down, 2 is the buffer
                {
                    if(is_nav_bar_up)
                        bottomNavigationViewAdapter.slideDown();
                    is_nav_bar_up = false;
                }
                else if (scrollView.getScrollY() < y - scrollAnimBuffer)
                {
                    if(!is_nav_bar_up)
                        bottomNavigationViewAdapter.slideUp();
                    is_nav_bar_up = true;
                }
                y= scrollView.getScrollY();
            }
        });

        TextView ausdNewsSelector = view.findViewById(R.id.home_ausdNews_selector),
                communitySelector = view.findViewById(R.id.home_community_selector);


        SpannableStringBuilder builder = new SpannableStringBuilder("AUSD News");
        builder.setSpan(new StyleSpan(Typeface.BOLD),0,4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ausdNewsSelector.setText(builder);

        communitySelector.setOnClickListener(view1 -> {
            if (news_tab_selected == 0) {
                news_tab_selected = 1;
                ausdNewsSelector.setBackgroundResource(R.drawable.home_ausdnews_bg_inactive);
                ausdNewsSelector.setTextColor(getResources().getColor(R.color.brightRed, getActivity().getTheme()));
                communitySelector.setBackgroundResource(R.drawable.home_community_bg_active);
                communitySelector.setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
                // switch to community tab
            }
        });

        ausdNewsSelector.setOnClickListener(view1 -> {
            if (news_tab_selected == 1) {
                news_tab_selected = 0;
                ausdNewsSelector.setBackgroundResource(R.drawable.home_ausdnews_bg_active);
                ausdNewsSelector.setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
                communitySelector.setBackgroundResource(R.drawable.home_community_bg_inactive);
                communitySelector.setTextColor(getResources().getColor(R.color.teal, getActivity().getTheme()));
                // switch to ausd news tab
            }
        });


        TextView monthText = view.findViewById(R.id.home_monthDate);
        String month = new SimpleDateFormat("MMMM ", Locale.US).format(Calendar.getInstance().getTimeInMillis()); // note space
        monthText.setText(month);
        TextView dayText = view.findViewById(R.id.home_dayDate);
        String day = new SimpleDateFormat("d", Locale.US).format(Calendar.getInstance().getTimeInMillis());
        dayText.setText(day);

        setUpFeaturedArticleViewPager(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setUpFeaturedArticleViewPager(View view){
        featuredArticleViewPager = view.findViewById(R.id.home_featured_carousel);

        articleList = new ArrayList<>();
//

//        featuredArticleViewPager.setAdapter(new FeaturedArticleAdapter(articleList));
//
//        featuredArticleViewPager.setClipToPadding(false);
//        featuredArticleViewPager.setClipChildren(false);
//        featuredArticleViewPager.setOffscreenPageLimit(3);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        //margin determines distance between two pages
        //adjust left/right padding of viewpager2 to determine distance between left and right edges and current page
        compositePageTransformer.addTransformer(new MarginPageTransformer((int) dp_to_px(0))); //note: conversion between dp and pixel, apply later
        compositePageTransformer.addTransformer(new ScaleAndFadeTransformer());
//        featuredArticleViewPager.setPageTransformer(compositePageTransformer);
    }

    public float dp_to_px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }


}
package com.hsappdev.ahs.UI.home;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.hsappdev.ahs.BottomNavigationCallback;
import com.hsappdev.ahs.Helper;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
                .replace(R.id.home_news_fragment_holder, homeNewsFragment) //replace instead of add
                .commit();


        NestedScrollView scrollView = view.findViewById(R.id.home_scrollView);
        final float scrollAnimBuffer = 4;
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
        Helper.setBoldRegularText(ausdNewsSelector, "AUSD", " News");

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



        TextView dateText = view.findViewById(R.id.home_date);
        String month = new SimpleDateFormat("MMMM ", Locale.US).format(Calendar.getInstance().getTimeInMillis()); // note space
        String day = new SimpleDateFormat("d", Locale.US).format(Calendar.getInstance().getTimeInMillis());
        Helper.setBoldRegularText(dateText, month, day);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }




}
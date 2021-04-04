package com.hsappdev.ahs.UI.home;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private HomeViewModel mViewModel;
    private Fragment homeNewsFragment, communityFragment;

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
        homeNewsFragment = new HomeNewsFragment();
        communityFragment = new HomeCommunityFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_news_fragment_holder, homeNewsFragment) //replace instead of add
                .add(R.id.home_news_fragment_holder, communityFragment)
                .hide(communityFragment)
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

        TextViewSelector ausdNewsSelector = view.findViewById(R.id.home_ausdNews_selector),
                communitySelector = view.findViewById(R.id.home_community_selector);
        Helper.setBoldRegularText(ausdNewsSelector, "AUSD", " News");


        ausdNewsSelector.setOnSelectedListener(() -> {
            news_tab_selected = 0;
            communitySelector.setSelected(false);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .show(homeNewsFragment)
                    .hide(communityFragment)
                    .commit();
        });
        AtomicBoolean hasAddedCommunityFrag = new AtomicBoolean(false);
        communitySelector.setOnSelectedListener(() -> {
            news_tab_selected = 1;
            ausdNewsSelector.setSelected(false);
            /*if(hasAddedCommunityFrag.get())*/
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .show(communityFragment)
                        .hide(homeNewsFragment)
                        .commit();
            /*else {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .add(R.id.home_news_fragment_holder, communityFragment)
                        .show(communityFragment)
                        .hide(homeNewsFragment)
                        .commit();
                hasAddedCommunityFrag.set(true);
            }*/
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
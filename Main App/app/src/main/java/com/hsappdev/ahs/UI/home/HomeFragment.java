package com.hsappdev.ahs.UI.home;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.hsappdev.ahs.BottomNavigationAdapter;
import com.hsappdev.ahs.R;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private BottomNavigationAdapter bottomNavigationViewAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomNavigationViewAdapter = (BottomNavigationAdapter) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    private boolean is_nav_bar_up = true;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }


}
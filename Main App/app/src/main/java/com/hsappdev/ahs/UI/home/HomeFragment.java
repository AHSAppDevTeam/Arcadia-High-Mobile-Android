package com.hsappdev.ahs.UI.home;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hsappdev.ahs.BottomNavigationCallback;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.OnNotificationSectionClicked;
import com.hsappdev.ahs.UI.home.community.HomeCommunityFragment;
import com.hsappdev.ahs.UI.home.search.ArticleSearchView;
import com.hsappdev.ahs.UI.home.search.SearchInterface;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private Fragment homeNewsFragment, communityFragment;
    private boolean isSearchSelected = false;
    private AlertDialog dialog;
    private LinearLayout selectorLinearLayout;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private BottomNavigationCallback bottomNavigationViewAdapter;
    private OnNotificationSectionClicked onNotificationSectionClicked;
    private OnItemClick onItemClick;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomNavigationViewAdapter = (BottomNavigationCallback) context;
            onNotificationSectionClicked = (OnNotificationSectionClicked) context;
            onItemClick = (OnItemClick) context;

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

        selectorLinearLayout = view.findViewById(R.id.home_selector_linear_layout);

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
        ImageView searchSelector = view.findViewById(R.id.home_search_selector);
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

        communitySelector.setOnSelectedListener(() -> {
            news_tab_selected = 1;
            ausdNewsSelector.setSelected(false);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .show(communityFragment)
                    .hide(homeNewsFragment)
                    .commit();

        });
        createDialog();
        searchSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                searchSelector.setSelected(false);
//            }
//        });

        // Notification Button
        Button notifButton = view.findViewById(R.id.home_notification_button);
        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationSectionClicked.onNotificationSectionClicked();
            }
        });

        TextView dateText = view.findViewById(R.id.home_date);
        String month = new SimpleDateFormat("MMMM ", Locale.US).format(Calendar.getInstance().getTimeInMillis()); // note space
        String day = new SimpleDateFormat("d", Locale.US).format(Calendar.getInstance().getTimeInMillis());
        Helper.setBoldRegularText(dateText, month, day);

        return view;
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(new SearchInterface(getContext(), getLayoutInflater(), getActivity().getApplication(), onItemClick));
        dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;

        // Get the selectors y coordinate
        int y = (int) (selectorLinearLayout.getY()+selectorLinearLayout.getHeight());
        layoutParams.y = y;

    }


}
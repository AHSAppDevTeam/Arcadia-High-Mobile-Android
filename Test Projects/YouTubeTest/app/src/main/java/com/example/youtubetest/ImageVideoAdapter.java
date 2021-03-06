package com.example.youtubetest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class ImageVideoAdapter extends FragmentStateAdapter {
    private static final String TAG = "ImageVideoAdapter";
    private final Media[] mediaList;
    private final Fragment[] fragments;
    final private YoutubeVideoCallback<YouTubeFragment> youtubeVideoCallback;

    public ImageVideoAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, YoutubeVideoCallback<YouTubeFragment> youtubeVideoCallback, ViewPager2 viewPager2, Media[] mediaList) {
        super(fragmentManager, lifecycle);
        this.youtubeVideoCallback = youtubeVideoCallback;
        this.mediaList = mediaList;
        this.fragments = new Fragment[mediaList.length];

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                youtubeVideoCallback.releaseAll();
                if(fragments[position] instanceof YouTubeFragment) {
                    ((YouTubeFragment) fragments[position]).initVideo();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(mediaList[position].isVideo()) {
            YouTubeFragment fragment = new YouTubeFragment(mediaList[position], youtubeVideoCallback);
            fragments[position] = fragment;
        } else {
            ImageFragment fragment = new ImageFragment(mediaList[position]);
            fragments[position] = fragment;
        }
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return mediaList.length;
    }

}

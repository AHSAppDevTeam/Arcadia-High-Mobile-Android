package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

import java.util.ArrayList;
import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NewsRecyclerAdapter";

    private static final int FEATURED = 0;
    private static final int CATEGORY = 1;
    private final Activity activity;
    private final ArrayList<String> categoryIDs;
    private final OnItemClick onArticleClick;

    public NewsRecyclerAdapter(ArrayList<String> categoryTitles, OnItemClick onArticleClick, Activity activity) {
        this.categoryIDs = categoryTitles;
        this.onArticleClick = onArticleClick;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FEATURED) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_news_featured_section, parent, false);
            return new FeaturedViewHolder(view, activity);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_news_category_section, parent, false);
            return new CategoryViewHolder(view, activity);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FEATURED;
        }
        return CATEGORY;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Log.d(TAG, "holder bind at position " + position +"\tcategory: " + categoryIDs.get(position));
        ((ViewGroup) holder.itemView).setClipChildren(false);
        ((ViewGroup) holder.itemView).setClipToPadding(false);
        if (holder.getItemViewType() == FEATURED) {
            ((FeaturedViewHolder) holder).setDetails(categoryIDs.get(position), onArticleClick);
        } else {
            ((CategoryViewHolder) holder).setDetails(categoryIDs.get(position), onArticleClick);
        }
    }

    @Override
    public int getItemCount() {
        return categoryIDs.size();
    }

    public void addCategoryIDs(String ID) {
        categoryIDs.add(ID);
        notifyItemInserted(categoryIDs.size() - 1);
    }

    public void addCategoryIDs(List<String> IDs) {
        int oldpos = IDs.size();
        this.categoryIDs.addAll(IDs);
        notifyItemRangeInserted(oldpos, this.categoryIDs.size() - 1);
    }

    public void clearAll() {
        categoryIDs.clear();
        notifyDataSetChanged();
    }


//    static class DefaultViewHolder extends RecyclerView.ViewHolder implements OnCategoryLoadedCallback {
//        private final TextView sectionTitle;
//        private final ViewPager2 featuredPager;
//        private final ViewPager2 smallPager;
//        private final TabLayout featuredTabLayout;
//        private final TabLayout smallTabLayout;
//        private final Resources r;
//        private final Activity activity;
//        FeaturedArticleAdapter featuredArticleAdapter;
//        MediumArticleAdapter mediumArticleAdapter;
//        SmallArticleAdapter smallArticleAdapter;
//
//        public void setDetails(String categoryTitle, OnItemClick onArticleClick) {
//            setUpPager();
//            featuredArticleAdapter = new FeaturedArticleAdapter(new ArrayList<String>(), onArticleClick, activity);
//            mediumArticleAdapter = new MediumArticleAdapter(new ArrayList<String>(), onArticleClick, activity);
//            smallArticleAdapter = new SmallArticleAdapter(new ArrayList<String>(), onArticleClick, activity);
//
//            featuredPager.setAdapter(featuredArticleAdapter);
//            smallPager.setAdapter(smallArticleAdapter);
//
//            CategoryLoader.getInstance().getCategory(categoryTitle, r, this);
//
//
//        }
//
//        @Override
//        public void onCategoryLoaded(Category category) {
//            featuredArticleAdapter.clearAll();
//            mediumArticleAdapter.clearAll();
//            smallArticleAdapter.clearAll();
//            Log.d(TAG, "onCategoryLoaded: size" + category.getCategoryID() + category.getArticleIds());
//            articleSortingJunction(new ArrayList<>(category.getArticleIds()), category.getTitle(), featuredArticleAdapter, mediumArticleAdapter, smallArticleAdapter);
//            // set section title
//            String regularText = " News";
//            Helper.setBoldRegularText(sectionTitle, category.getTitle(), regularText);
//            sectionTitle.setTextColor(category.getColor());
//        }
//
//        /**
//         * Sort articles into the correct categories
//         *
//         * @param featuredArticleAdapter
//         * @param mediumArticleAdapter
//         * @param smallArticleAdapter
//         */
//        public void articleSortingJunction(List<String> articleIds, String categoryTitle,
//                                           FeaturedArticleAdapter featuredArticleAdapter,
//                                           MediumArticleAdapter mediumArticleAdapter,
//                                           SmallArticleAdapter smallArticleAdapter) {
//
//            /*
//             * Structure
//             * > one large article for most recent
//             * > two medium articles for second and third most recent
//             * > rest of articles go into the small viewpager
//             * */
//            if (articleIds.size() > 0) {
//                featuredArticleAdapter.addArticleIds(articleIds.get(0));
//                articleIds.remove(0);
//            }
//            for (int i = 0; i < MultiArticleAdapter.numArticles; i++) {
//                if (articleIds.size() > 0) {
//                    mediumArticleAdapter.addArticleId(articleIds.get(0));
//                    articleIds.remove(0);
//                } else {
//                    break;
//                }
//            }
//
//            smallArticleAdapter.setArticleIds(articleIds); // Add rest of ids
//
//            setUpTabLayout(featuredArticleAdapter, mediumArticleAdapter, smallArticleAdapter);
//        }
//
//        private void setUpTabLayout(FeaturedArticleAdapter featuredArticleAdapter,
//                                    MediumArticleAdapter mediumArticleAdapter,
//                                    SmallArticleAdapter smallArticleAdapter) {
//            if (featuredArticleAdapter.getItemCount() > 1) {
//                featuredTabLayout.setVisibility(View.VISIBLE);
//                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(featuredTabLayout, featuredPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
//                    @Override
//                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                    }
//                });
//                tabLayoutMediator.attach();
//            } else {
//                featuredTabLayout.setVisibility(View.GONE);
//            }
//
//
//
//            if (smallArticleAdapter.getItemCount() > 1) {
//                smallTabLayout.setVisibility(View.VISIBLE);
//
//                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(smallTabLayout, smallPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
//                    @Override
//                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                    }
//                });
//                tabLayoutMediator.attach();
//            } else {
//                smallTabLayout.setVisibility(View.GONE);
//
//            }
//        }
//
//        public void setUpPager() {
//            featuredPager.setOffscreenPageLimit(3);
//
//            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//            //margin determines distance between two pages
//            //adjust left/right padding of viewpager2 to determine distance between left and right edges and current page
//            //Log.d(TAG, "Dimen value: " + r.getDimension(R.dimen.padding));
//            compositePageTransformer.addTransformer(new MarginPageTransformer((int) dp_to_px(2))); //note: conversion between dp and pixel, apply later
//            compositePageTransformer.addTransformer(new ScaleAndFadeTransformer(false));
//            featuredPager.setPageTransformer(compositePageTransformer);
//
//            smallPager.setOffscreenPageLimit(3);
//
//        }
//
//        public float dp_to_px(float dp) {
//            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
//        }
//
//        public DefaultViewHolder(@NonNull View itemView, Activity activity) {
//            super(itemView);
//            r = itemView.getContext().getResources();
//
//            featuredPager = itemView.findViewById(R.id.home_featured_carousel);
//            smallPager = itemView.findViewById(R.id.home_small_carousel);
//            sectionTitle = itemView.findViewById(R.id.home_news_sectionTitle);
//
//            featuredTabLayout = itemView.findViewById(R.id.featured_tab_layout);
//            smallTabLayout = itemView.findViewById(R.id.small_tab_layout);
//
//            this.activity = activity;
//        }
//    }
}
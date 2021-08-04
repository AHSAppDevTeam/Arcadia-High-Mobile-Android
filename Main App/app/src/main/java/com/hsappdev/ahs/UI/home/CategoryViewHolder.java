package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.bulletin.CategoryLoadedCallback;
import com.hsappdev.ahs.cache.CategoryLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.cache.callbacks.ArticleLoadableCallback;
import com.hsappdev.ahs.cache.callbacks.CategoryLoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.localdb.SavedDatabase;
import com.hsappdev.ahs.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements CategoryLoadableCallback {
    private static final String TAG = "CategoryViewHolder";

    private final TextView sectionTitle;
    private final ViewPager2 smallPager;
    private final LinearLayout categoryLinearLayout;
    private final TabLayout smallTabLayout;
    private final Resources r;
    private final Activity activity;
    private OnItemClick onItemClick;

    SmallArticleAdapter smallArticleAdapter;

    public void setDetails(String categoryTitle, OnItemClick onArticleClick) {
        onItemClick = onArticleClick;

        setUpPager();

        smallArticleAdapter = new SmallArticleAdapter(new ArrayList<String>(), onArticleClick, activity);

        smallPager.setAdapter(smallArticleAdapter);

        //CategoryLoader.getInstance().getCategory(categoryTitle, r, this);
        CategoryLoaderBackend.getInstance(activity.getApplication()).getCacheObject(categoryTitle, r, this);
    }


    /**
     * Sort articles into the correct categories
     *
     * @param smallArticleAdapter
     */
    public void articleSortingJunction(List<String> articleIds,
                                       String categoryTitle,
                                       SmallArticleAdapter smallArticleAdapter) {
        categoryLinearLayout.removeAllViews();
        /*
         * Structure
         * > one large article for most recent
         * > two medium articles for second and third most recent
         * > rest of articles go into the small viewpager
         * */
        if (articleIds.size() > 0) {
            setUpLargeArticle(articleIds.get(0), onItemClick);
            articleIds.remove(0);
        }
        List<String> mediumArticleIds = new ArrayList<>();
        for (int i = 0; i < MultiArticleAdapter.numArticles; i++) {
            if (articleIds.size() > 0) {
                mediumArticleIds.add(articleIds.get(0));
                articleIds.remove(0);
            } else {
                break;
            }
        }
        setUpMediumArticles(mediumArticleIds, onItemClick);

        smallArticleAdapter.setArticleIds(articleIds); // Add rest of ids

        setUpTabLayout(smallArticleAdapter);
    }

    private void setUpLargeArticle(String articleID, OnItemClick onItemClick) {
        View view = LayoutInflater.from(categoryLinearLayout.getContext()).inflate(R.layout.home_news_category_article, null, false);
        LargeArticleUnit articleUnit = new LargeArticleUnit(view, onItemClick, activity);
        articleUnit.setDetails(articleID);
        categoryLinearLayout.addView(view);
    }

    private void setUpMediumArticles(List<String> articles, OnItemClick onItemClick){
        LinearLayout intermediateLinearLayout = new LinearLayout(categoryLinearLayout.getContext());

        for(String article : articles){
            MediumArticleUnit unit = new MediumArticleUnit(categoryLinearLayout.getContext(), article, onItemClick, R.layout.home_news_medium_article, activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            unit.setLayoutParams(params);
            intermediateLinearLayout.addView(unit);
            // Add Divider
            if(!article.equals(articles.get(articles.size()-1)) || articles.size() == 1) { // if not the last article
                Space space = new Space(categoryLinearLayout.getContext());
                LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                        r.getDimensionPixelSize(R.dimen.padding),
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                space.setLayoutParams(spaceParams);
                intermediateLinearLayout.addView(space);
            }
        }
        if(articles.size() == 1) { // if there is only one article, add an empty article
            View unit = new View(categoryLinearLayout.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            unit.setLayoutParams(params);
            intermediateLinearLayout.addView(unit);
        }
        categoryLinearLayout.addView(intermediateLinearLayout);
    }

    private void setUpTabLayout(SmallArticleAdapter smallArticleAdapter) {

        if (smallArticleAdapter.getItemCount() > 1) {
            smallTabLayout.setVisibility(View.VISIBLE);

            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(smallTabLayout, smallPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                }
            });
            tabLayoutMediator.attach();
        } else {
            smallTabLayout.setVisibility(View.GONE);

        }
    }

    public void setUpPager() {
        smallPager.setOffscreenPageLimit(3);
    }


    public CategoryViewHolder(@NonNull View itemView, Activity activity) {
        super(itemView);
        r = itemView.getContext().getResources();

        smallPager = itemView.findViewById(R.id.home_small_carousel);
        sectionTitle = itemView.findViewById(R.id.home_news_sectionTitle);
        categoryLinearLayout = itemView.findViewById(R.id.home_category_linearLayout);
        smallTabLayout = itemView.findViewById(R.id.small_tab_layout);

        this.activity = activity;
    }

    @Override
    public void onLoaded(Category category) {
        smallArticleAdapter.clearAll();
        //Log.d(TAG, "onCategoryLoaded: size" + category.getCategoryID() + category.getArticleIds());
        articleSortingJunction(new ArrayList<>(category.getArticleIds()), category.getTitle(), smallArticleAdapter);
        // set section title
        String regularText = " News";
        Helper.setBoldRegularText(sectionTitle, category.getTitle(), regularText);
        sectionTitle.setTextColor(category.getColor());
    }

    @Override
    public boolean isActivityDestroyed() {
        return activity.isDestroyed();
    }
}

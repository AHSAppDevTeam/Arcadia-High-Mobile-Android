package com.hsappdev.ahs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.UI.home.ScaleAndFadeTransformer;
import com.hsappdev.ahs.UI.home.article.RelatedArticleAdapter;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.CommunitySection;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.localdb.ArticleRepository;
import com.hsappdev.ahs.mediaPager.ImageVideoAdapter;
import com.hsappdev.ahs.mediaPager.ImageViewActivity;
import com.hsappdev.ahs.mediaPager.Media;
import com.hsappdev.ahs.mediaPager.OnImageClick;
import com.hsappdev.ahs.mediaPager.YouTubeFragment;
import com.hsappdev.ahs.mediaPager.YoutubeVideoCallback;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.util.ScreenUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements Adjusting_TextView.hello, OnImageClick, OnItemClick {

    private static final String TAG = "ArticleActivity";

    public static final String data_KEY = "0";

    private Article article;

    private PopupWindow fontBarWindow;
    private ViewPager2 mediaViewPager;
    private TabLayout tabLayout;
    private RecyclerView relatedArticles;
    private Button seeMoreSectionButton;
    private RelatedArticleAdapter relatedArticlesAdapter;
    private YoutubeVideoCallback<YouTubeFragment> youtubeVideoCallback;

    private RequestQueue queue;


    private ArrayList<Adjusting_TextView.TextSizeCallback> callbackList = new ArrayList<>();
    ArticleRepository articleRepository;

    public ArticleActivity() {
        youtubeVideoCallback = new YoutubeVideoCallback<>();
    }

    @Override
    public void addTextSizeCallback(Adjusting_TextView.TextSizeCallback callback) {
        callbackList.add(callback);
    }

    private boolean isArticleSaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        articleRepository = new ArticleRepository(getApplication()); // note: should be in a view model
        setContentView(R.layout.article);

        queue = Volley.newRequestQueue(this);

        final ArticleViewModel viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        viewModel.getTextScalar().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float newTextScalar) {
                for (Adjusting_TextView.TextSizeCallback callback : callbackList) {
                    callback.onTextSizeChanged(newTextScalar);
                }
            }
        });

        article = getIntent().getParcelableExtra(data_KEY);
        View outer = findViewById(R.id.article_linearLayout);
        TextView title = findViewById(R.id.article_title);
        TextView author = findViewById(R.id.article_author);
        TextView body = findViewById(R.id.article_body);
        LinearLayout fontBar = findViewById(R.id.article_font_adjuster);
        title.setText(article.getTitle());
        author.setText("By " + article.getAuthor());
        ScreenUtil.setHTMLStringToTextView(article.getBody(), body);
        if(article.getIsViewed() == 0) { // if it is not viewed
            incrementViews(article.getArticleID());
        }

        // Media ViewPager2
        mediaViewPager = findViewById(R.id.mediaViewPager);
        tabLayout = findViewById(R.id.article_tab_layout);

        TabLayoutMediator tabLayoutMediator = null;
        if (article.getImageURLs().length > 1) {

            tabLayoutMediator = new TabLayoutMediator(tabLayout, mediaViewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                }
            });
        }

        // Prepare mediaList
        Media[] mediaList = new Media[article.getImageURLs().length + article.getVideoURLs().length];
        int i = 0;
        for (String video : article.getVideoURLs()) {
            mediaList[i] = new Media(video, true);
            i++;
        }
        for (String image : article.getImageURLs()) {
            mediaList[i] = new Media(image, false);
            i++;
        }

        ImageVideoAdapter imageVideoAdapter = new ImageVideoAdapter(getSupportFragmentManager(), getLifecycle(), youtubeVideoCallback, mediaViewPager, mediaList, this);

        mediaViewPager.setAdapter(imageVideoAdapter);
        if (tabLayoutMediator != null) {
            tabLayoutMediator.attach();
        }

        mediaViewPager.setOffscreenPageLimit(3);

        if(mediaList.length == 0){
            mediaViewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        //margin determines distance between two pages
        //adjust left/right padding of viewpager2 to determine distance between left and right edges and current page
        //Log.d(TAG, "Dimen value: " + r.getDimension(R.dimen.padding));
        compositePageTransformer.addTransformer(new MarginPageTransformer((int) ScreenUtil.dp_to_px(2, this))); //note: conversion between dp and pixel, apply later
        compositePageTransformer.addTransformer(new ScaleAndFadeTransformer(true));
        mediaViewPager.setPageTransformer(compositePageTransformer);

        // Toolbar
        MaterialToolbar articleToolbar = findViewById(R.id.article_topAppBar);
        articleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissFontWindow();
                finish();
                overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
            }
        });

        final Context context = this;
        // do async or whatever
        articleRepository.isArticleSaved(article.getArticleID()).observe(this, isSaved -> {
            isArticleSaved = isSaved;

            setSavedIcon(articleToolbar.getMenu().getItem(2), isSaved);
        });

        articleToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.article_toolbar_font:
                        setUpTextSizeAdjuster(viewModel, articleToolbar, context);
                        return true;
                    case R.id.article_toolbar_theme:
                        SettingsManager settingsManager = SettingsManager.getInstance(getApplicationContext());
                        settingsManager.updateNightMode(!settingsManager.isNightModeOn());
                        AppCompatDelegate.setDefaultNightMode(settingsManager.isNightModeOn() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                        getWindow().setWindowAnimations(R.style.WindowAnimationTransition);
                        recreate();
                        return true;
                    case R.id.article_toolbar_saved:
                        if(isArticleSaved)
                            article.setIsSaved(0);
                        else
                            article.setIsSaved(1);

                        articleRepository.add(article);
                        isArticleSaved = !isArticleSaved;
                        setSavedIcon(item, isArticleSaved);
                        return true;
                }
                return false;
            }
        });

        // Set details for toolbar
        articleToolbar.setTitle(Helper.getSpanBoldRegularText(article.getCategoryDisplayName(), " News"));
        articleToolbar.setTitleTextAppearance(this, R.style.ArticleAppBarTitleFont);
        articleToolbar.setTitleTextColor(article.getCategoryDisplayColor());

        relatedArticles = findViewById(R.id.article_related_articles);
        seeMoreSectionButton = findViewById(R.id.article_see_more_section_button);
        setUpRelatedArticlesSection();

    }

    private void setUpRelatedArticlesSection() {

        int padding = (int) getResources().getDimension(R.dimen.padding);
        relatedArticles.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = padding;
                outRect.left = padding;
                if(parent.getChildAdapterPosition(view) == 0){
                    outRect.top = padding;
                }
                outRect.bottom = padding;

            }
        });

        seeMoreSectionButton.setText("See more in " + Helper.getSpanBoldRegularText(article.getCategoryDisplayName(), ""));
        seeMoreSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunitySection communitySection = new CommunitySection(article.getCategoryID(),
                        article.getCategoryDisplayName(),
                        article.getCategoryDisplayName(),
                        article.getCategoryDisplayColor(),
                        article.isFeatured());
                Intent intent = new Intent(ArticleActivity.this, CommunityActivity.class);
                intent.putExtra(CommunityActivity.DATA_KEY, communitySection);
                startActivity(intent);
            }
        });
        // We need to load related articles from firebase
        // Related articles constantly change and are not cached for this reason
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).getReference()
                .child(getResources().getString(R.string.db_articles))
                .child(article.getArticleID());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean hasLoaded = false; // Limit to one load

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(hasLoaded) return;
                hasLoaded = true;
                List<String> relatedArticleIds = new ArrayList<>();
                for (DataSnapshot articleId : snapshot.child(getResources().getString(R.string.db_articles_related_articles)).getChildren()) {
                    relatedArticleIds.add(articleId.getValue(String.class));
                    Log.d("relatedArticle", articleId.getValue(String.class));
                    loadIndividualRelatedArticles(relatedArticleIds);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void loadIndividualRelatedArticles(List<String> relatedArticleIds) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(relatedArticles.getContext(), LinearLayoutManager.VERTICAL, false);
        relatedArticles.setLayoutManager(layoutManager);
        relatedArticlesAdapter = new RelatedArticleAdapter(relatedArticleIds, this, this);
        relatedArticles.setAdapter(relatedArticlesAdapter);
    }

    private void incrementViews(String articleID) {
        final String url = getString(R.string.firebaseFunctionsIncrementView);
        final JSONObject jsonBody;
        article.setIsViewed(1); // 1 == true (SQL lite does not support boolean)
        // Save the article
        articleRepository.add(article);
        try {
            jsonBody = new JSONObject("{\"data\":{\"id\":\""+articleID+"\"}}");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,null, null);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpTextSizeAdjuster(ArticleViewModel viewModel, Toolbar articleToolbar, Context context) {
        View view = getLayoutInflater().inflate(R.layout.article_textsizeadjustor, null);

        fontBarWindow = new PopupWindow(context);
        fontBarWindow.setContentView(view);

        // Fontbar
        SeekBar fontSeekbar = view.findViewById(R.id.article_font_adjuster_seekbar);
        int progress = (int) (fontSeekbar.getMax() * (viewModel.getTextScalar().getValue() - 0.5f));
        fontSeekbar.setProgress(progress);
        TextView fontSizeDisplay = view.findViewById(R.id.article_font_size_display);
        fontSizeDisplay.setText(Integer.toString(progress));
        fontSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float convertedProgress = ((float) progress) / seekBar.getMax() + 0.5f;
                //Log.d(TAG, String.valueOf(convertedProgress));
                viewModel.setTextScalar(convertedProgress);
                fontSizeDisplay.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fontBarWindow.dismiss();
            }
        });

        fontBarWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.article_font_bar_background));
        fontBarWindow.setElevation(getResources().getDimension(R.dimen.elevation));

        fontBarWindow.setOutsideTouchable(true);
        fontBarWindow.setTouchable(true);

        int padding = (int) getResources().getDimension(R.dimen.padding);
        fontBarWindow.setWidth((articleToolbar.getWidth() - 2 * padding));
        fontBarWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        fontBarWindow.showAsDropDown(articleToolbar, padding, padding / 2);
    }

    private void setSavedIcon(MenuItem item, boolean isSaved) {
        if (isSaved) {
            item.setIcon(R.drawable.article_appbar_saved_ic);
            item.setTitle("Don't save this article");
        } else {
            item.setIcon(R.drawable.article_appbar_not_saved_ic);
            item.setTitle("Save this article");
        }
    }
/*
    private void addToSavedDatabase(MenuItem item) {
        if (articleRepository.isArticleSaved(article.getArticleID())) {
            articleRepository.delete(article.getArticleID());
        } else {
            articleRepository.add(article);
        }
        setSavedIcon(item);
    }*/

    private void dismissFontWindow() {
        if (fontBarWindow != null) {
            fontBarWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissFontWindow();
        // Animation on back press
        overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
    }

    public void adjustFontScale(Configuration config, float scale) {
        config.fontScale = scale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        metrics.scaledDensity = config.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(config, metrics);
    }

    @Override
    public void onImageClick(Media media) {
        Intent intent = new Intent(ArticleActivity.this, ImageViewActivity.class);
        intent.putExtra(ImageViewActivity.IMAGE_URL_KEY, media.getMediaURL());
        startActivity(intent);
    }

    @Override
    public void onArticleClicked(Article article) {
        Intent intent = new Intent(ArticleActivity.this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.data_KEY, article);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);

    }
}
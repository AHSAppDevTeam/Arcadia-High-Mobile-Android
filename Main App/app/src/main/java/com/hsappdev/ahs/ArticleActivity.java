package com.hsappdev.ahs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.hsappdev.ahs.UI.home.ScaleAndFadeTransformer;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.SavedDatabase;
import com.hsappdev.ahs.mediaPager.ImageViewActivity;
import com.hsappdev.ahs.mediaPager.OnImageClick;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.util.ScreenUtil;
import com.hsappdev.ahs.mediaPager.ImageVideoAdapter;
import com.hsappdev.ahs.mediaPager.Media;
import com.hsappdev.ahs.mediaPager.YouTubeFragment;
import com.hsappdev.ahs.mediaPager.YoutubeVideoCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArticleActivity extends AppCompatActivity implements Adjusting_TextView.hello, OnImageClick {

    private static final String TAG = "ArticleActivity";

    static final String data_KEY = "0";
    private boolean fontBarIsOpen = false;
    private final int FONT_BAR_MIN = 18;
    private final int FONT_BAR_MAX = 54;

    Article article;

    PopupWindow fontBarWindow;
    ViewPager2 mediaViewPager;
    TabLayout tabLayout;
    private YoutubeVideoCallback<YouTubeFragment> youtubeVideoCallback;

    RequestQueue queue;


    private ArrayList<Adjusting_TextView.TextSizeCallback> callbackList = new ArrayList<>();

    public ArticleActivity() {
        youtubeVideoCallback = new YoutubeVideoCallback<>();
    }

    @Override
    public void addTextSizeCallback(Adjusting_TextView.TextSizeCallback callback) {
        callbackList.add(callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        incrementViews(article.getArticleID());

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
                // overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
            }
        });

        final Context context = this;

        setSavedIcon(articleToolbar.getMenu().getItem(2));
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
                        addToSavedDatabase(item);
                        return true;
                }
                return false;
            }
        });

        // Set details for toolbar
        articleToolbar.setTitle(Helper.getSpanBoldRegularText(article.getCategoryDisplayName(), " News"));
        articleToolbar.setTitleTextAppearance(this, R.style.ArticleAppBarTitleFont);
        articleToolbar.setTitleTextColor(article.getCategoryDisplayColor());


    }

    private void incrementViews(String articleID) {
        final String url = getString(R.string.firebaseFunctionsIncrementView);
        final JSONObject jsonBody;
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

    private void setSavedIcon(MenuItem item) {
        SavedDatabase savedDatabase = SavedDatabase.getInstance(getApplicationContext());
        if (savedDatabase.articleIsSaved(article)) {
            item.setIcon(R.drawable.article_appbar_saved_ic);
            item.setTitle("Don't save this article");
        } else {
            item.setIcon(R.drawable.article_appbar_not_saved_ic);
            item.setTitle("Save this article");
        }
    }

    private void addToSavedDatabase(MenuItem item) {
        SavedDatabase savedDatabase = SavedDatabase.getInstance(getApplicationContext());
        if (savedDatabase.articleIsSaved(article)) {
            savedDatabase.remove(article);
        } else {
            savedDatabase.add(article);
        }
        setSavedIcon(item);
    }

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
        // overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
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
}
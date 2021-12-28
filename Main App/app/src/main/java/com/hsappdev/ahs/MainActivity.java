package com.hsappdev.ahs;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.hsappdev.ahs.UI.home.OnSectionClicked;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.callbacks.ArticleLoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.CommunitySection;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.firebaseMessaging.NotificationSetup;
import com.hsappdev.ahs.localdb.ArticleRepository;

public class MainActivity extends AppCompatActivity implements BottomNavigationCallback, SettingsManager.DayNightCallback, OnItemClick, OnSectionClicked, OnNotificationSectionClicked {

    private BottomNavigationView navView;
    private static final String TAG = "MainActivity";
    private CardView draggableSheetCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //      WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SettingsManager settingsManager = SettingsManager.getInstance(getApplicationContext());
        boolean nightModeOn = settingsManager.isNightModeOn();

        // update dark mode theme before views are drawn
        if(nightModeOn)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Resources r =getResources();


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(r.getString(R.string.db_app_id))
                .setApiKey(r.getString(R.string.db_api_key))
                .setDatabaseUrl(r.getString(R.string.db_url))
                .build();

        if(FirebaseApp.getApps(getApplicationContext()).size() == 1) {
            FirebaseApp.initializeApp(getApplicationContext(), options, DatabaseConstants.FIREBASE_REALTIME_DB);
        }
        ArticleRepository articleRepository = new ArticleRepository(this.getApplication());
        if (getIntent().getExtras() != null) {
            String articleID = (String) getIntent().getExtras().get("articleID");
            ArticleLoaderBackend.getInstance((Application) getApplicationContext()).getCacheObject(articleID, getResources(), new ArticleLoadableCallback() {
                private boolean isFirstTime = false;
                @Override
                public void onLoaded(Article article) {
                    article.setIsNotification(1); // 1 == true
                    articleRepository.add(article); // To save the notification
                    onArticleClicked(article);
                    isFirstTime = true; // used to mimic activity destruction and remove this listener
                }

                @Override
                public boolean isActivityDestroyed() {
                    // return MainActivity.this.isDestroyed();
                    return isFirstTime;
                }
            });
        }

        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        /*CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                navView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());*/
        navView.setOnApplyWindowInsetsListener(null);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navView, navController);

        navView.setItemIconTintList(null); // Remove tint from navbar; Required for navbar icons to work

        ////////////////// NOTIFICATIONS //////////////////

        NotificationSetup.setUp(getResources(), this);

        // TODO: REMOVE THIS WHEN DEPLOYING THE APP
        if(BuildConfig.DEBUG) {
            NotificationSetup.subscribe(this, "Drafts");
        }

        setUpNotificationIcon();


        /////////////// Draggable Card View ////////////////
        draggableSheetCardView = findViewById(R.id.draggableSheetCardView);
        BottomSheetBehavior<CardView> bottomSheetBehavior = BottomSheetBehavior.from(draggableSheetCardView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Animate the corners
                float cardRadius = getResources().getDimensionPixelSize(R.dimen.padding);
                draggableSheetCardView.setRadius((1f-slideOffset)*cardRadius);
            }
        });

    }

    private void setUpNotificationIcon() {

        // First initially setup the notif icon
        // pretend that all notifs are read

        // TODO: notif button


        ArticleRepository articleRepository = new ArticleRepository(getApplication());
        articleRepository.getAllNotificationArticles().observe(this, articleList -> {
            boolean isAllRead = true;
            for (int i = 0; i < articleList.size(); i++) {
                if(articleList.get(i).getIsViewed() == 0) {
                    isAllRead = false;
                }
            }

            if (!isAllRead) {
                // show the special icon
                // TODO: need icons
            }
        });
    }

    @Override
    public void slideUp(){
//        navView.animate().translationY(0).setDuration(500);
            /*view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);*/
    }


    // slide the view from its current position to below itself
    @Override
    public void slideDown(){
        //navView.animate().translationY(navView.getHeight()).setDuration(500);
//            /*TranslateAnimation animate = new TranslateAnimation(
//                    0,                 // fromXDelta
//                    0,                 // toXDelta
//                    0,                 // fromYDelta
//                    view.getHeight()); // toYDelta
//            animate.setDuration(500);
//            animate.setFillAfter(true);
//            view.startAnimation(animate);*/
    }

    @Override
    public void onNewMode(boolean isNightModeOn) {
        AppCompatDelegate.setDefaultNightMode(isNightModeOn ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setWindowAnimations(R.style.WindowAnimationTransition);
        recreate();
    }

    @Override
    public void onArticleClicked(Article article) {
        Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.data_KEY, article);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);
    }

    @Override
    public void onClicked(CommunitySection communitySection) {
        Intent intent = new Intent(MainActivity.this, CommunityActivity.class);
        intent.putExtra(CommunityActivity.DATA_KEY, communitySection);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);
    }

    @Override
    public void onNotificationSectionClicked() {
        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            //hideSystemUI();
//        }
//    }
//
//    @Override
//    public void onRestart() {
//        super.onRestart();
//        //hideSystemUI();
//    }
//
//    private void hideSystemUI() {
//        // Enables regular immersive mode.
//        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
//        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        // Set the content to appear under the system bars so that the
//                        // content doesn't resize when the system bars hide and show.
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        // Hide the nav bar and status bar
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
//    }
}
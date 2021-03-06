package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;
import com.hsappdev.ahs.UI.home.OnSectionClicked;
import com.hsappdev.ahs.cache.ArticleCategoryIdLoader;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.CommunitySection;
import com.hsappdev.ahs.firebaseMessaging.NotificationSetup;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements BottomNavigationCallback, SettingsManager.DayNightCallback, OnItemClick, OnSectionClicked, OnNotificationSectionClicked {

    private BottomNavigationView navView;
    private static final String TAG = "MainActivity";

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

        FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB)).setPersistenceEnabled(true);

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

        // FIXME: REMOVE THIS WHEN DEPLOYING THE APP
        if(BuildConfig.DEBUG) {
            NotificationSetup.subscribe(this, "Drafts");
        }

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
//        navView.animate().translationY(navView.getHeight()).setDuration(500);
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
        // overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);
    }

    @Override
    public void onClicked(CommunitySection communitySection) {
        Intent intent = new Intent(MainActivity.this, CommunityActivity.class);
        intent.putExtra(CommunityActivity.DATA_KEY, communitySection);
        startActivity(intent);
        // overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);
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
package com.hsappdev.ahs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.viewModels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainActivityViewModel viewModel;
    private Resources r;

    // Views
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        r = getResources();

        initializeFirebase();

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        handleNavigation();

    }

    /**
     * Sets up the navigation bar
     */
    private void handleNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    /**
     * Creates a firebase instance with id = DatabaseConstants.FIREBASE_REALTIME_DB
     */
    private void initializeFirebase() {
        Resources r = getResources();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(r.getString(R.string.db_app_id))
                .setApiKey(r.getString(R.string.db_api_key))
                .setDatabaseUrl(r.getString(R.string.db_url))
                .build();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        if(FirebaseApp.getApps(getApplicationContext()).size() == 1) {
            FirebaseApp.initializeApp(getApplicationContext(), options, DatabaseConstants.FIREBASE_REALTIME_DB);
        }
    }
}
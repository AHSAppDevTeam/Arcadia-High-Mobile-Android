package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.TestDataLoadingViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.hsappdev.ahs.dataTypes.BoardDataType;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.ui_new.reusable.recyclerview.AbstractDataRecyclerView;

import java.util.List;
import java.util.ResourceBundle;

public class TestDataLoadingActivity extends AppCompatActivity {

    private static final String TAG = "TestDataLoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data_loading);


        /* START COPY FIREBASE INIT FROM MainActivity */
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

        /* END COPY FIREBASE INIT FROM MainActivity */


        final TestDataLoadingViewModel viewModel = new ViewModelProvider(this).get(TestDataLoadingViewModel.class);

        // Recycler view stuff
        AbstractDataRecyclerView<BoardDataType> dataRecyclerViewAdapter = new AbstractDataRecyclerView<>();
        dataRecyclerViewAdapter.setViewId(R.layout.test_data_loading_board_view);
        RecyclerView recyclerView = findViewById(R.id.boardsRecyclerView);

        recyclerView.setAdapter(dataRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        viewModel.getArticles().observe(this, new Observer<List<BoardDataType>>() {
            @Override
            public void onChanged(List<BoardDataType> boardsList) {
                Log.d(TAG, String.format("List Size: %d", boardsList.size()));
                dataRecyclerViewAdapter.setDataList(boardsList);
            }
        });

    }
}
package com;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.BoardDataType;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

public class TestDataLoadingViewModel extends ViewModel {

    private static final String TAG = "TestDataLoadingViewMode";

    private final MutableLiveData<List<BoardDataType>> articlesMutableLiveData = new MutableLiveData<>();

    public LiveData<List<BoardDataType>> getArticles() {
        return articlesMutableLiveData;
    }

    public TestDataLoadingViewModel() {
        // start loading data
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB))
                .getReference()
                .child("boards");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // iterate through the boards
                List<BoardDataType> boardsList = new ArrayList<>();
                for(DataSnapshot boardSnapshot : snapshot.getChildren()){
                    String title = boardSnapshot.child("title").getValue(String.class);
                    BoardDataType boardDataType = new BoardDataType(new ArrayList<>(), 0, 0, title);
                    boardsList.add(boardDataType);
                }

                Log.d(TAG, String.format("List Size: %d", boardsList.size()));

                articlesMutableLiveData.setValue(boardsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

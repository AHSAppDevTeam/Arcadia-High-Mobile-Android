package com.hsappdev.ahs.viewModels;

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
import com.hsappdev.ahs.newDataTypes.ArticleDataType;
import com.hsappdev.ahs.newDataTypes.BoardDataType;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "TestDataLoadingViewMode";

    private final MutableLiveData<List<BoardDataType>> boardsMutableLiveData = new MutableLiveData<>();

    public LiveData<List<BoardDataType>> getBoards() {
        return boardsMutableLiveData;
    }

    public MainActivityViewModel() {
        // start loading data
        startLoadingBoardsData();

    }

    public void startLoadingBoardsData() {
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
                    long editTimestamp = boardSnapshot.child("editTimestamp").getValue(Long.class);
                    int sort = boardSnapshot.child("editTimestamp").getValue(Integer.class);

                    for(DataSnapshot articleIdList : snapshot.child("articleIDs").getChildren()) {
                        String articleId = articleIdList.getValue(String.class);
                        Log.d(TAG, String.format("Article Id: %s", articleId));

                    }

                    BoardDataType boardDataType = new BoardDataType(new ArrayList<>(), editTimestamp, sort, title);
                    boardsList.add(boardDataType);
                }

                Log.d(TAG, String.format("List Size: %d", boardsList.size()));

                boardsMutableLiveData.setValue(boardsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

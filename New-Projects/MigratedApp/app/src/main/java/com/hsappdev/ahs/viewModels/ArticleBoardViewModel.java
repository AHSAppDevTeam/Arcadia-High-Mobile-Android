package com.hsappdev.ahs.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.newDataTypes.ArticleDataType;
import com.hsappdev.ahs.newDataTypes.BoardDataType;

import java.util.ArrayList;
import java.util.List;

public class ArticleBoardViewModel extends ViewModel {

    private static final String TAG = "ArticleBoardViewModel";

    // do not modify array
    private ArrayList<String> articleIds;

    private final MutableLiveData<List<ArticleDataType>> articleMutableLiveData = new MutableLiveData<>();


    public ArticleBoardViewModel() {

    }

    public void setArticleIds (ArrayList<String> articleIds) {
        this.articleIds = articleIds;
        startLoadingArticleData();
    }

    private void startLoadingArticleData() {
        for (String articleId : articleIds) {
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB))
                    .getReference()
                    .child("articles")
                    .child(articleId);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // iterate through the boards
                    List<ArticleDataType> boardsList = new ArrayList<>();
                    for (DataSnapshot boardSnapshot : snapshot.getChildren()) {
                        Log.d(TAG, String.format("Article Id: %s", articleId));

                        // BoardDataType boardDataType = new BoardDataType(new ArrayList<>(), editTimestamp, sort, title);
                        // boardsList.add(boardDataType);
                    }

                    Log.d(TAG, String.format("List Size: %d", boardsList.size()));

                    // articleMutableLiveData.postValue();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}

package com.hsappdev.ahs.viewModels;

import android.content.res.Resources;
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
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.newDataTypes.ArticleDataType;

import java.util.ArrayList;
import java.util.List;

public class ArticleBoardViewModel extends ViewModel {

    private static final String TAG = "ArticleBoardViewModel";

    // do not modify array
    private ArrayList<String> articleIds;

    // functions as a "collector" (collects articles as they load)
    private List<ArticleDataType> articleDataListCollector = new ArrayList<>();

    private final MutableLiveData<List<ArticleDataType>> articleMutableLiveData = new MutableLiveData<>();

    public LiveData<List<ArticleDataType>> getArticles() {
        return articleMutableLiveData;
    }


    public ArticleBoardViewModel() {
    }

    public void startLoadingArticleData(ArrayList<String> articleIds, Resources r) {
        this.articleIds = articleIds;
        startLoadingArticleData(r);
    }

    private void startLoadingArticleData(Resources r) {
        for (String articleId : articleIds) {
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB))
                    .getReference()
                    .child("articles")
                    .child(articleId);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // extract values and set
                    Log.d(TAG, String.format("Article Id: %s", articleId));

                    ArticleDataType article = new ArticleDataType();

                    String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                    String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                    String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                    String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
                    ArrayList<String> imageURLs = new ArrayList<>();
                    ArrayList<String> videoURLs = new ArrayList<>();
                    for (DataSnapshot imageURL : snapshot.child(r.getString(R.string.db_articles_imageURLs)).getChildren()) {
                        imageURLs.add(imageURL.getValue(String.class));
                    }
                    for (DataSnapshot videoURL : snapshot.child(r.getString(R.string.db_articles_videoURLs)).getChildren()) {
                        videoURLs.add(videoURL.getValue(String.class));
                    }

                    Long extractedTimestamp = snapshot.child(r.getString(R.string.db_articles_timestamp)).getValue(long.class);

                    long timestamp = (extractedTimestamp == null) ? 0 : extractedTimestamp;

                    article.setArticleID(articleId);
                    article.setAuthor(author);
                    article.setTitle(title);
                    article.setBody(body);
                    article.setCategoryID(category);
                    article.setImageURLs(imageURLs.toArray(new String[0]));
                    article.setVideoURLs(videoURLs.toArray(new String[0]));
                    article.setTimestamp(timestamp);

                    articleDataListCollector.add(article);

                    articleMutableLiveData.setValue(new ArrayList<>(articleDataListCollector)); // must make new copy

                    Log.d(TAG, article.toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}

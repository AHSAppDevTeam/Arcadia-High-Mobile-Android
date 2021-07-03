package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.hsappdev.ahs.UI.saved.SavedRecyclerAdapter;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.List;

public class NotificationActivity extends AppCompatActivity implements OnItemClick {

    private static final String TAG = "NotificationActivity";
    
    private ArticleRepository articleRepository;
    private RecyclerView recyclerView;
    private SavedRecyclerAdapter savedRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setUpRecyclerView();

        loadNotifsFromDatabase();
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.notification_recycler_view);
        savedRecyclerAdapter = new SavedRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(savedRecyclerAdapter);
    }



    private void loadNotifsFromDatabase() {
        articleRepository = new ArticleRepository(getApplication());
        articleRepository.getAllNotificationArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleList) {
                savedRecyclerAdapter.replaceAll(articleList);
                Log.d(TAG, "onChanged: " + articleList.size());
            }
        });
    }

    @Override
    public void onArticleClicked(Article article) {

    }
}
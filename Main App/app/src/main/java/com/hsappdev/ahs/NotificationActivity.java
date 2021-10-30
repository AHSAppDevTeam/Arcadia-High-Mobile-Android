package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hsappdev.ahs.UI.notification.NotificationRecyclerAdapter;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.List;

public class NotificationActivity extends AppCompatActivity implements OnItemClick {

    private static final String TAG = "NotificationActivity";
    
    private ArticleRepository articleRepository;
    private RecyclerView recyclerView;
    private NotificationRecyclerAdapter notificationRecyclerAdapter;

    private int sortMode = 0;


    private LinearLayout clearAllButton;
    private Spinner sortBySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        clearAllButton = findViewById(R.id.notification_clear_all);
        sortBySpinner = findViewById(R.id.notification_sort_by_spinner);

        setUpRecyclerView();

        loadNotifsFromDatabase();
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.notification_recycler_view);
        notificationRecyclerAdapter = new NotificationRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(notificationRecyclerAdapter);
        setUpNotificationControls();
    }

    private void setUpNotificationControls() {
        clearAllButton.setOnClickListener(v -> new AlertDialog.Builder(NotificationActivity.this)
                .setTitle("Delete Request")
                .setIcon(R.drawable.article_appbar_not_saved_ic)
                .setMessage("Do you really want to clear all notifications?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        articleRepository.deleteAll();
                        //emptyMsgTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(NotificationActivity.this, "All notifications cleared", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show());
        ArrayAdapter adapter = ArrayAdapter.createFromResource(NotificationActivity.this, R.array.saved_pager_spinner_options, R.layout.default_spinner_layout);
        adapter.setDropDownViewResource(R.layout.default_spinner_layout);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortMode = position;
                notificationRecyclerAdapter.onSortModeChanged(sortMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void loadNotifsFromDatabase() {
        articleRepository = new ArticleRepository(getApplication());
        articleRepository.getAllNotificationArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleList) {
                notificationRecyclerAdapter.replaceAll(articleList);
                Log.d(TAG, "onChanged: " + articleList.size());
            }
        });
    }

    @Override
    public void onArticleClicked(Article article) {
        Intent intent = new Intent(NotificationActivity.this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.data_KEY, article);
        startActivity(intent);
    }
}
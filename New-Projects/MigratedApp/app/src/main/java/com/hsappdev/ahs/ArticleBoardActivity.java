package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.hsappdev.ahs.viewModels.ArticleBoardViewModel;
import com.hsappdev.ahs.viewModels.MainActivityViewModel;

import java.util.ArrayList;

public class ArticleBoardActivity extends AppCompatActivity {

    public static final String ARTICLE_BOARD_TITLE_DATA_KEY = "ARTICLE_TITLE_ID";
    private String title;

    public static final String ARTICLE_IDS_DATA_KEY = "ARTICLE_IDS";
    private ArrayList<String> articleIds;

    private ArticleBoardViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_board);

        extractDataFromIntent();

        viewModel = new ViewModelProvider(this).get(ArticleBoardViewModel.class);
        viewModel.setArticleIds(articleIds);

        setupUI();
    }

    private void setupUI() {
        // title
        TextView titleText = findViewById(R.id.board_articles_title_bold);
        titleText.setText(title);

        // recycler view

    }

    private void extractDataFromIntent() {
        if(getIntent() != null) {
            title = getIntent().getStringExtra(ARTICLE_BOARD_TITLE_DATA_KEY);
            articleIds = getIntent().getStringArrayListExtra(ARTICLE_IDS_DATA_KEY);
        } else {
            // something is wrong, go back home
            finish();
        }
    }
}
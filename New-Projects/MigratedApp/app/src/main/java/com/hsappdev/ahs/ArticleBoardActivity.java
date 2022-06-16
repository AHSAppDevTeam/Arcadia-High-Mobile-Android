package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hsappdev.ahs.newDataTypes.ArticleDataType;
import com.hsappdev.ahs.newDataTypes.BoardDataType;
import com.hsappdev.ahs.ui.reusable.recyclerview.AbstractDataRecyclerView;
import com.hsappdev.ahs.viewModels.ArticleBoardViewModel;
import com.hsappdev.ahs.viewModels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleBoardActivity extends AppCompatActivity {

    private static final String TAG = "ArticleBoardActivity";

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
        viewModel.startLoadingArticleData(articleIds, getResources());

        setupUI();
    }

    private void setupUI() {
        // title
        TextView titleText = findViewById(R.id.board_articles_title_bold);
        titleText.setText(title);

        // recycler view
        // Recycler view stuff
        AbstractDataRecyclerView<ArticleDataType> dataRecyclerViewAdapter = new AbstractDataRecyclerView<>();
        dataRecyclerViewAdapter.setViewId(R.layout.home_board_category_section);
        RecyclerView recyclerView = findViewById(R.id.articleBoardRecyclerView);

        recyclerView.setAdapter(dataRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        viewModel.getArticles().observe(this, new Observer<List<ArticleDataType>>() {
            @Override
            public void onChanged(List<ArticleDataType> boardsList) {
                Log.d(TAG, String.format("List Size: %d", boardsList.size()));
                dataRecyclerViewAdapter.setDataList(boardsList);
            }
        });

    }

    private void extractDataFromIntent() {
        if(getIntent() != null) {
            title = getIntent().getStringExtra(ARTICLE_BOARD_TITLE_DATA_KEY);
            articleIds = getIntent().getStringArrayListExtra(ARTICLE_IDS_DATA_KEY);

            // TODO: override here
            articleIds = new ArrayList<>();
            articleIds.add("cyan-pearl-magenta");
            articleIds.add("lemon-snow-quincy");
            articleIds.add("magenta-toolbox-jonquil");
            articleIds.add("mango-fulvous-fawn");

        } else {
            // something is wrong, go back home
            finish();
        }
    }
}
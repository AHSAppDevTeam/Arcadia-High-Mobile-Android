package com.hsappdev.ahs.UI.home.search;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.List;

public class SearchInterface {
    private RecyclerView searchRecycler;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private ArticleRepository articleRepository;

    private View view;

    private static final String TAG = "SearchInterface";

    public SearchInterface(LayoutInflater layoutInflater, Application application, OnItemClick onItemClick) {
        articleRepository = new ArticleRepository(application);
        articleRepository.getAllArticles().observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleList) {
                Log.d(TAG, "onChanged: Load search");
                searchRecyclerAdapter.setArticles(articleList);
            }
        });
        createView(layoutInflater, onItemClick);
    }

    public void createView(LayoutInflater layoutInflater, OnItemClick onItemClick){
        View view = layoutInflater.inflate(R.layout.search_bar_dialog, null, false);
        this.view = view;
        searchRecycler = view.findViewById(R.id.search_recyclerView);
        ArticleSearchView searchView = view.findViewById(R.id.article_searchView);
        searchView.setSearchInterface(this);

        searchRecyclerAdapter = new SearchRecyclerAdapter(onItemClick);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(searchRecycler.getContext(), LinearLayoutManager.VERTICAL, false);
        searchRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(searchRecycler.getContext(),
                DividerItemDecoration.VERTICAL);
        searchRecycler.addItemDecoration(dividerItemDecoration);
        searchRecycler.setAdapter(searchRecyclerAdapter);

    }

    public void onUpdate(String query) {
        searchRecyclerAdapter.onUpdate(query);
    }

    public View getView() {
        return view;
    }
}

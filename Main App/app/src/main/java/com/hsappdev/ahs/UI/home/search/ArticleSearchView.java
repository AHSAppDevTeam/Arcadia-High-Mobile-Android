package com.hsappdev.ahs.UI.home.search;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SearchView;

public class ArticleSearchView extends SearchView implements SearchView.OnQueryTextListener {
    private SearchInterface searchInterface;
    public ArticleSearchView(Context context) {
        super(context);
        setUp();
    }

    public ArticleSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }



    public void setUp(){
        this.setOnQueryTextListener(this);
        this.setIconifiedByDefault(false);
        this.requestFocus();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchInterface.onUpdate(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchInterface.onUpdate(newText);
        return true;
    }

    public SearchInterface getSearchInterface() {
        return searchInterface;
    }

    public void setSearchInterface(SearchInterface searchInterface) {
        this.searchInterface = searchInterface;
    }
}

package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleCategoryIdLoader;
import com.hsappdev.ahs.cache.CategoryListLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.cache.deprecated.OnCategoryListLoadedCallback;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.dataTypes.CategoryList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeNewsFragment extends Fragment implements LoadableCallback {
    private static final String TAG = "HomeNewsFragment";

    private OnItemClick onArticleClick;
    private NewsRecyclerAdapter adapter;


    public HomeNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onArticleClick = (OnItemClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

 //MainActivity -> HomeFragment (home page) ->
    // HomeNewsFragment (AUSD news section) get locations and structure , pass into-> RecyclerViewAdapter -> ViewPager2
    private ArrayList<String> categoryTitles = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Resources r = getResources();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_news_fragment, container, false);

       adapter = new NewsRecyclerAdapter(new ArrayList<String>(), onArticleClick, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.home_news_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryListLoaderBackend.getInstance(getActivity().getApplication()).getCacheObject(r.getString(R.string.db_location_ausdNews), r, this);
        return view;
    }

    @Override
    public <T> void onLoaded(T article) {
        CategoryList categoryList = (CategoryList) article;
        adapter.clearAll();
        adapter.addCategoryIDs(categoryList.getCategoryList());

    }

    @Override
    public boolean isActivityDestroyed() {
        return getActivity().isDestroyed();
    }
}
package com.hsappdev.ahs.UI.home;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.CategoryListLoaderBackend;
import com.hsappdev.ahs.cache.callbacks.CategoryListLoadableCallback;
import com.hsappdev.ahs.cache_new.CategoryListLoaderBackEnd;
import com.hsappdev.ahs.cache_new.DataLoaderBackEnd;
import com.hsappdev.ahs.dataTypes.CategoryList;
import com.hsappdev.ahs.localdb.CategoryListRepository;

import java.util.ArrayList;

public class HomeNewsFragment extends Fragment implements CategoryListLoadableCallback {
    private static final String TAG = "HomeNewsFragment";

    private OnItemClick onArticleClick;
    private NewsRecyclerAdapter adapter;


    public HomeNewsFragment() {
        // Required empty public constructor
    }

    private Activity hostActivity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onArticleClick = (OnItemClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
        hostActivity = (Activity) context;
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

       adapter = new NewsRecyclerAdapter(new ArrayList<String>(), onArticleClick, (AppCompatActivity) getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.home_news_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        CategoryListLoaderBackEnd loader = new CategoryListLoaderBackEnd(
                r.getString(R.string.db_location_ausdNews),
                r,
                new CategoryListRepository(getActivity().getApplication()));
        loader.getLiveData().observe(getViewLifecycleOwner(), new Observer<DataLoaderBackEnd.DataWithSource<CategoryList>>() {
            @Override
            public void onChanged(DataLoaderBackEnd.DataWithSource<CategoryList> categoryListDataWithSource) {
                CategoryList categoryList = categoryListDataWithSource.getData();
                adapter.clearAll();
                adapter.addCategoryIDs(categoryList.getCategoryList());
            }
        });

        /*CategoryListLoaderBackend.getInstance(getActivity().getApplication()).getCacheObject(r.getString(R.string.db_location_ausdNews), r, this);*/
        return view;
    }

    @Override
    public void onLoaded(CategoryList categoryList) {
        adapter.clearAll();
        adapter.addCategoryIDs(categoryList.getCategoryList());

    }

    @Override
    public boolean isActivityDestroyed() {
        return hostActivity.isDestroyed();
    }
}
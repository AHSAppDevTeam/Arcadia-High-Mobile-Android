package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.BottomNavigationCallback;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleCategoryIdLoader;
import com.hsappdev.ahs.cache.OnCategoryListLoadedCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeNewsFragment extends Fragment implements OnCategoryListLoadedCallback {
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
        ArticleCategoryIdLoader.loadCategoryList(r, this);





        return view;
    }

    public void updateCategory(String newCategoryReference) {

    }

    @Override
    public void categoryListLoaded(List<String> categoryList) {
        adapter.addCategoryIDs(categoryList);
    }
}
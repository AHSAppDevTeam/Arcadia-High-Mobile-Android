package com.hsappdev.ahs.UI.saved;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.SavedDatabase;
import com.hsappdev.ahs.util.Helper;

public class SavedFragment extends Fragment {
    private static final String TAG = "SavedFragment";

    private SavedViewModel mViewModel;

    private RecyclerView savedRecyclerView;
    private SavedRecyclerAdapter savedRecyclerAdapter;

    private TextView titleTextView;
    private TextView emptyMsgTextView;
    private LinearLayout clearAllButton;
    private OnItemClick onArticleClick;
    private Spinner sortBySpinner;


    // Sort by options
    private static final String NEWEST = "Sort by Newest";
    private static final int NEWEST_ID = 0;
    private static final String OLDEST = "Sort by Oldest";
    private static final int OLDEST_ID = 1;

    private int sortMode = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onArticleClick = (OnItemClick) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved, container, false);

        savedRecyclerView = view.findViewById(R.id.saved_fragment_recycler_view);
        titleTextView = view.findViewById(R.id.saved_fragment_title);
        clearAllButton = view.findViewById(R.id.saved_fragment_clear_all);
        emptyMsgTextView = view.findViewById(R.id.saved_fragment_empty_message);
        sortBySpinner = view.findViewById(R.id.saved_fragment_sort_by_spinner);
        setUpRecyclerView();
        initView();
        return view;
    }

    private void initView() {
        Helper.setBoldRegularText(titleTextView, "Your", " Saved");
        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedDatabase savedDatabase = SavedDatabase.getInstance(getActivity().getApplicationContext());
                savedDatabase.deleteAll();
                savedRecyclerAdapter.clearAll();
                emptyMsgTextView.setVisibility(View.VISIBLE);
            }
        });
        String[] items = new String[]{NEWEST, OLDEST};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.default_spinner_layout, items);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + position);
                sortMode = position;
                savedRecyclerAdapter.onSortModeChanged(sortMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SavedViewModel.class);
    }

    private void setUpRecyclerView() {
        savedRecyclerAdapter = new SavedRecyclerAdapter(onArticleClick);
        SavedDatabase savedDatabase = SavedDatabase.getInstance(getActivity().getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        savedRecyclerView.setLayoutManager(linearLayoutManager);
        savedRecyclerView.setAdapter(savedRecyclerAdapter);
        savedDatabase.loadAllSavedArticles(new SavedDatabase.ArticleLoadedCallback() {
            @Override
            public void onArticleLoaded(Article article) {
                savedRecyclerAdapter.addArticle(article);
                emptyMsgTextView.setVisibility(View.GONE);
                Log.d(TAG, "onArticleLoaded: " + article.getArticleID());
            }
        });
    }

}
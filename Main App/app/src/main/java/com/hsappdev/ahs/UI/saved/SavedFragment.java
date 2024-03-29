package com.hsappdev.ahs.UI.saved;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
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


    private int sortMode = 0;
    private SavedViewModel model;

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
        clearAllButton = view.findViewById(R.id.notification_clear_all);
        emptyMsgTextView = view.findViewById(R.id.saved_fragment_empty_message);
        sortBySpinner = view.findViewById(R.id.notification_sort_by_spinner);
        setUpRecyclerView();
        initView();
        return view;
    }

    private void initView() {
        Helper.setBoldRegularText(titleTextView, "Your", " Saved");
        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Request")
                        .setIcon(R.drawable.article_appbar_not_saved_ic)
                        .setMessage("Do you really want to clear all saved articles?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //SavedDatabase savedDatabase = SavedDatabase.getInstance(getActivity().getApplicationContext());
                                model.deleteAll();
                                emptyMsgTextView.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "All saved articles cleared", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.saved_pager_spinner_options, R.layout.default_spinner_layout);
        adapter.setDropDownViewResource(R.layout.default_spinner_layout);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + position);
                sortMode = position;
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                savedRecyclerAdapter.onSortModeChanged(sortMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //sortBySpinner.setSelection(0, true);

        int padding = getResources().getDimensionPixelSize(R.dimen.padding);
        savedRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = padding;
                outRect.left = padding;
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = padding;
                }
                outRect.bottom = padding;

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(SavedViewModel.class);
    }

/*
    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    public void onRefresh() {
        emptyMsgTextView.setVisibility(View.VISIBLE);
        savedRecyclerAdapter.clearAll();
        SavedDatabase savedDatabase = SavedDatabase.getInstance(getActivity().getApplicationContext());
        savedDatabase.loadAllSavedArticles(new SavedDatabase.ArticleLoadedCallback() {
            @Override
            public void onArticleLoaded(Article article) {
                savedRecyclerAdapter.addArticle(article);
                emptyMsgTextView.setVisibility(View.GONE);
                Log.d(TAG, "onArticleLoaded: " + article.getArticleID());
            }
        });
    }*/

    private void setUpRecyclerView() {
        savedRecyclerAdapter = new SavedRecyclerAdapter(onArticleClick);
        /*SavedDatabase savedDatabase = SavedDatabase.getInstance(getActivity().getApplicationContext());*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        savedRecyclerView.setLayoutManager(linearLayoutManager);
        savedRecyclerView.setAdapter(savedRecyclerAdapter);

        model.getAllSavedArticles().observe(getViewLifecycleOwner(), articles -> {
            savedRecyclerAdapter.replaceAll(articles);
            emptyMsgTextView.setVisibility(/*(articles.size() == 0) ? View.VISIBLE : */View.GONE);
        });
        /*savedDatabase.loadAllSavedArticles(new SavedDatabase.ArticleLoadedCallback() {
            @Override
            public void onArticleLoaded(Article article) {
                savedRecyclerAdapter.addArticle(article);
                emptyMsgTextView.setVisibility(View.GONE);
                Log.d(TAG, "onArticleLoaded: " + article.getArticleID());
            }
        });*/
    }

}
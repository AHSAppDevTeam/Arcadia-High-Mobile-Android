package com.hsappdev.ahs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsappdev.ahs.newDataTypes.BoardDataType;
import com.hsappdev.ahs.ui.reusable.recyclerview.AbstractDataRecyclerView;
import com.hsappdev.ahs.viewModels.MainActivityViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private MainActivityViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // initialize with arguments
        }

        // get the view model shared between the classes
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);


    }

    private void setupRecyclerView(View view) {
        // Recycler view stuff
        AbstractDataRecyclerView<BoardDataType> dataRecyclerViewAdapter = new AbstractDataRecyclerView<>();
        dataRecyclerViewAdapter.setViewId(R.layout.home_board_category_section);
        RecyclerView recyclerView = view.findViewById(R.id.boardsRecyclerView);

        recyclerView.setAdapter(dataRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));


        viewModel.getArticles().observe(requireActivity(), new Observer<List<BoardDataType>>() {
            @Override
            public void onChanged(List<BoardDataType> boardsList) {
                Log.d(TAG, String.format("List Size: %d", boardsList.size()));
                dataRecyclerViewAdapter.setDataList(boardsList);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupRecyclerView(view);

        return view;
    }
}
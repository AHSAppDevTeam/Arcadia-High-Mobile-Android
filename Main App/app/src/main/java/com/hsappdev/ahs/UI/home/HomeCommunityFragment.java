package com.hsappdev.ahs.UI.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsappdev.ahs.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeCommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeCommunityFragment extends Fragment {

    public HomeCommunityFragment() {
        // Required empty public constructor
    }


    public static HomeCommunityFragment newInstance(String param1, String param2) {
        HomeCommunityFragment fragment = new HomeCommunityFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_community_fragment, container, false);
    }
}
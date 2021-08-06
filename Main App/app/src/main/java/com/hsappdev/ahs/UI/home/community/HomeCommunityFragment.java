package com.hsappdev.ahs.UI.home.community;

import android.app.Activity;
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

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.home.OnSectionClicked;
import com.hsappdev.ahs.cache.CategoryListLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.cache.callbacks.CategoryListLoadableCallback;
import com.hsappdev.ahs.dataTypes.CategoryList;

public class HomeCommunityFragment extends Fragment implements CategoryListLoadableCallback {

    private static final String TAG = "HomeCommunityFragment";

    private CommunityRecyclerAdapter communityRecyclerAdapter;
    private RecyclerView recyclerView;
    private View contentView;
    private OnSectionClicked onCommunityClick;
    private Resources r;

    public HomeCommunityFragment() {

    }

    private Activity hostActivity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onCommunityClick = (OnSectionClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
        hostActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_community_fragment, container, false);

        r = view.getResources();
        recyclerView = view.findViewById(R.id.home_community_recyclerview);
        communityRecyclerAdapter = new CommunityRecyclerAdapter(onCommunityClick);
        recyclerView.setAdapter(communityRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        this.contentView = view;

        CategoryListLoaderBackend
        .getInstance(getActivity().getApplication())
        .getCacheObject(r.getString(R.string.db_location_community), r, this);
        return view;
    }

    @Override
    public void onLoaded(CategoryList categoryList) {
        communityRecyclerAdapter.clearAll();
        communityRecyclerAdapter.addCommunitySections(categoryList.getCategoryList());
    }

    @Override
    public boolean isActivityDestroyed() {
        return hostActivity.isDestroyed();
    }
}
package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.CategoryLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeCommunityFragment extends Fragment {

    private static final String TAG = "HomeCommunityFragment";

    private CommunityRecyclerAdapter communityRecyclerAdapter;
    private RecyclerView recyclerView;
    private View contentView;
    private OnSectionClicked onCommunityClick;
    private Resources r;

    public HomeCommunityFragment() {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onCommunityClick = (OnSectionClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
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
        List<String> communitySections = new ArrayList<>();
        recyclerView = view.findViewById(R.id.home_community_recyclerview);
        communityRecyclerAdapter = new CommunityRecyclerAdapter(onCommunityClick);
        recyclerView.setAdapter(communityRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                .child(r.getString(R.string.db_locations))
                .child("community")
                .child(r.getString(R.string.db_locations_catID));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                communityRecyclerAdapter.clearAll();
                communitySections.clear(); // Make sure to clear this list
                for(DataSnapshot child : snapshot.getChildren()){
                    Log.d(TAG, "onDataChange: " + child.getValue(String.class));
                    communitySections.add(child.getValue(String.class));
                }
                Log.d(TAG, "onDataChange: length"+communitySections.size());
                communityRecyclerAdapter.addCommunitySections(communitySections);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.contentView = view;
        return view;
    }
}
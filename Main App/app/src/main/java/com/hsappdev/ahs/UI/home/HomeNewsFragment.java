package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;

import java.util.ArrayList;

public class HomeNewsFragment extends Fragment {
    private static final String TAG = "HomeNewsFragment";

    public HomeNewsFragment() {
        // Required empty public constructor
    }
 //MainActivity -> HomeFragment (home page) ->
    // HomeNewsFragment (AUSD news section) get locations and structure , pass into-> RecyclerViewAdapter -> ViewPager2
    private ArrayList<String> categoryTitles;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryTitles = new ArrayList<>();
        Resources r = getResources();

        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                .child(r.getString(R.string.database_locations_ref))
                .child(r.getString(R.string.database_ausdNews_ref))// homepage is default
                .child(r.getString(R.string.database_locations_catID_ref));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sectionTitle : snapshot.getChildren()) {
                    categoryTitles.add(sectionTitle.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_news_fragment, container, false);
        NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(categoryTitles);
        RecyclerView recyclerView = view.findViewById(R.id.home_new_recyclerView);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void updateCategory(String newCategoryReference) {

    }
}
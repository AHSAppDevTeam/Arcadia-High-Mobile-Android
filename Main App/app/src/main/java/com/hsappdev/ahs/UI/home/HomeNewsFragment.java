package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
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



        NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(new ArrayList<String>());
        RecyclerView recyclerView = view.findViewById(R.id.home_news_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                .child(r.getString(R.string.db_locations))
                .child(r.getString(R.string.db_location_ausdNews))// homepage is default
                .child(r.getString(R.string.db_locations_catID));
        //Log.d(TAG, "oncreateview");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG, "data changed");
                adapter.clearAll(); // if there is an update, prevent duplicate articles
                ArrayList<String> categoriesIDs = new ArrayList<>();
                for(DataSnapshot sectionTitle : snapshot.getChildren()) {
                    categoriesIDs.add(sectionTitle.getValue(String.class));


                }
                adapter.addCategoryIDs(categoriesIDs);
                //Log.d(TAG, "add to category titles");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    public void updateCategory(String newCategoryReference) {

    }
}
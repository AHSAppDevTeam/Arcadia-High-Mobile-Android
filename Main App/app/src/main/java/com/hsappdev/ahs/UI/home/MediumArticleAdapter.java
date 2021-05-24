package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.ArrayList;
import java.util.List;

public class MediumArticleAdapter extends RecyclerView.Adapter<MediumArticleAdapter.MediumArticleViewHolder>{
    private static final String TAG = "MediumArticleAdapter";
    private List<String> articleIds;
    private OnItemClick onArticleClick;

    private final int numStacked = 2;

    public MediumArticleAdapter(List<String> articleIds, OnItemClick onArticleClick) {
        this.articleIds = articleIds;
        this.onArticleClick = onArticleClick;
    }

    @NonNull
    @Override
    public MediumArticleAdapter.MediumArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediumArticleAdapter.MediumArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_medium_article_holder,
                        parent,
                        false
                ),
                onArticleClick
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MediumArticleAdapter.MediumArticleViewHolder holder, int position) {
        List<String> articlesToAdd = new ArrayList<>();

        // if we are not in an odd case
        if(position < articleIds.size()/numStacked){
            for (int index = 0; index<numStacked; index++){
                articlesToAdd.add(articleIds.get((position)*numStacked+index));
            }
        }else{
            // if we are in an odd case
            // then find the # of odd ones
            int oddOnes = articleIds.size()-(position)*numStacked;
            for (int index = 0; index<oddOnes; index++){
                articlesToAdd.add(articleIds.get((position)*numStacked+index));
            }
        }

        holder.setDetails(articlesToAdd);
    }

    @Override
    public int getItemCount() {
        if(articleIds.size() == 0){
            return 0;
        }
        if(articleIds.size()%numStacked == 0){
            return articleIds.size()/numStacked;
        }else{
            return articleIds.size()/numStacked+1;
        }
    }

    public void clearAll() {
        articleIds.clear();
        notifyDataSetChanged();
    }

    public void addArticleId(String articleId) {
        articleIds.add(articleId);
        notifyDataSetChanged();
    }

    static class MediumArticleViewHolder extends RecyclerView.ViewHolder{
        final private Resources r;
        private final LinearLayout linearLayoutLeft;
        private final LinearLayout linearLayoutRight;
        private OnItemClick onArticleClick;

        public MediumArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView);
            this.r = itemView.getResources();
            this.linearLayoutLeft = itemView.findViewById(R.id.home_news_medium_article_linear_layout_left);
            this.linearLayoutRight = itemView.findViewById(R.id.home_news_medium_article_linear_layout_right);
            this.onArticleClick = onArticleClick;


        }

        public void setDetails(List<String> articlesToAdd){
            linearLayoutLeft.removeAllViews();
            linearLayoutRight.removeAllViews();
            linearLayoutLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "click1");
                }
            });
            linearLayoutRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "click2");
                }
            });
            for(int i=0; i<articlesToAdd.size(); i++){
                String articleId = articlesToAdd.get(i);
                MediumArticleUnit mediumArticleUnit =  new MediumArticleUnit(itemView.getContext(), articleId, onArticleClick);
                // Alternate back and forth between the left and right linear layout
                // First start with the left linear layout
                if(i % 2 == 0) {
                    linearLayoutLeft.addView(mediumArticleUnit);
                }else{
                    linearLayoutRight.addView(mediumArticleUnit);
                }
            }
        }

    }
}

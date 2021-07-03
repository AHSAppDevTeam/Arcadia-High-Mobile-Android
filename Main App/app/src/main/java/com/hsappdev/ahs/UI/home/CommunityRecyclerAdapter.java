package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.CommunitySection;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.CommunityViewHolder>{

    private static final String TAG = "CommunityRecyclerAdapte";
    private List<String> communitySections;
    private OnSectionClicked onCommunityClick;

    public CommunityRecyclerAdapter(OnSectionClicked onCommunityClick) {
        this.communitySections = new ArrayList<String>();
        this.onCommunityClick = onCommunityClick;
    }

    public void clearAll(){
        communitySections.clear();
        notifyDataSetChanged();
    }

    public void addCommunitySections(List<String> newSections){
        communitySections.addAll(newSections);
        notifyDataSetChanged();
        //Log.i(TAG, "addCommunitySections: new" + communitySections.size() + " " + newSections.size());
    }


    @NonNull
    @Override
    public CommunityRecyclerAdapter.CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_community_section, parent, false);
        return new CommunityViewHolder(view, onCommunityClick);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityRecyclerAdapter.CommunityViewHolder holder, int position) {
        holder.setDetails(communitySections.get(position));
    }

    @Override
    public int getItemCount() {
        return communitySections.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final private OnSectionClicked onCommunityClick;
        final private Resources r;
        private CommunitySection communitySection;

        final private TextView categoryTitle;
        final private TextView categoryBlurb;

        final private View contentView;

        // Image Grid
        final private ImageView image1, image2, image3, image4;

        public void setDetails(String categoryId){
            contentView.setOnClickListener(this);
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance("database-access")).getReference()
                    .child(r.getString(R.string.db_categories))
                    .child(categoryId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);
                    String blurb = snapshot.child("blurb").getValue(String.class);
                    String color = snapshot.child("color").getValue(String.class);
                    boolean isFeatured = snapshot.child(r.getString(R.string.db_articles_featured)).getValue(boolean.class);
                    ArrayList<String> thumbURLs = new ArrayList<>();

                    for (DataSnapshot thumbURL : snapshot.child(r.getString(R.string.db_articles_thumbURLs)).getChildren()) {
                        thumbURLs.add(thumbURL.getValue(String.class));
                    }

                    communitySection = new CommunitySection(categoryId, title, blurb, color,  isFeatured, thumbURLs.toArray(new String[0]));
                    categoryTitle.setTextColor(Color.parseColor(communitySection.getDisplayColor()));
                    Helper.setBoldRegularText(categoryTitle, communitySection.getCategoryDisplayName(), " News");
                    categoryBlurb.setText(communitySection.getBlurb());
                    setImages();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void setImages() {
            for (int i = 0; i < communitySection.getThumbURLs().length; i++) {
                String url = communitySection.getThumbURLs()[i];
                switch (i) {
                    case 0:
                        ImageUtil.setImageToView(url, image1);
                    case 1:
                        ImageUtil.setImageToView(url, image2);
                    case 2:
                        ImageUtil.setImageToView(url, image3);
                    case 3:
                        ImageUtil.setImageToView(url, image4);
                }

            }
        }

        public CommunityViewHolder(View itemView, OnSectionClicked onCommunityClick) {
            super(itemView);
            this.contentView = itemView.findViewById(R.id.community_frameLayout);
            this.onCommunityClick = onCommunityClick;
            this.r = itemView.getResources();

            this.categoryTitle = itemView.findViewById(R.id.community_section_name);
            this.categoryBlurb = itemView.findViewById(R.id.community_section_blurb);

            this.image1 = itemView.findViewById(R.id.community_image_1);
            this.image2 = itemView.findViewById(R.id.community_image_2);
            this.image3 = itemView.findViewById(R.id.community_image_3);
            this.image4 = itemView.findViewById(R.id.community_image_4);

        }

        @Override
        public void onClick(View v) {
            onCommunityClick.onClicked(communitySection);
        }
    }
}

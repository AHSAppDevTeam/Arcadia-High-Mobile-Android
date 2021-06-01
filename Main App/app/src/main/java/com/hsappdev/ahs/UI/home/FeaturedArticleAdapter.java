package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class FeaturedArticleAdapter extends RecyclerView.Adapter<FeaturedArticleAdapter.FeaturedArticleViewHolder> {
    private static final String TAG = "FeaturedArticleAdapter";
    private List<String> articleIds;
    private OnItemClick onArticleClick;

    public FeaturedArticleAdapter(List<String> articleIds, OnItemClick onArticleClick) {
        this.articleIds = articleIds;
        this.onArticleClick = onArticleClick;
    }

    // GETTERS AND SETTERS
    public List<String> getArticleIds() {
        return articleIds;
    }

    public void addArticleIds(String articleId) {
        articleIds.add(articleId);
        notifyItemInserted(articleIds.size()-1);
    }
    public void clearAll() {
        articleIds.clear();
        notifyDataSetChanged();
    }
    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeaturedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeaturedArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_featured_article,
                        parent,
                        false
                ),
                onArticleClick
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedArticleViewHolder holder, int position) {
        holder.setDetails(articleIds.get(position));
    }

    @Override
    public int getItemCount() {
        return articleIds.size();
    }

    static class FeaturedArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Article article;
        final private ConstraintLayout articleLayout;
        final private ImageView articleImage;
        final private Resources r;
        final private TextView titleTextView;
        final private TextView timeTextView;
        final private TextView categoryTextView;
        final private ImageView indicatorImageView;
        private OnItemClick onArticleClick;

        public FeaturedArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView);
            this.r = itemView.getResources();
            this.articleLayout = itemView.findViewById(R.id.home_news_constraintLayout);
            this.articleImage = itemView.findViewById(R.id.featured_article_image);
            this.titleTextView = itemView.findViewById(R.id.featured_article_name);
            this.categoryTextView = itemView.findViewById(R.id.featured_article_category);
            this.indicatorImageView = itemView.findViewById(R.id.featured_article_indicator);
            this.timeTextView = itemView.findViewById(R.id.featured_article_time);
            this.onArticleClick = onArticleClick;

        }

        public void setDetails(String articleID){
            articleLayout.setOnClickListener(this);
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(r.getString(R.string.db_articles))
                    .child(articleID);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                    String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                    String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                    String category = snapshot.child(r.getString(R.string.db_articles_categoryID)).getValue(String.class);
                    ArrayList<String> imageURLs = new ArrayList<>();
                    ArrayList<String> videoURLs = new ArrayList<>();
                    for(DataSnapshot imageURL: snapshot.child(r.getString(R.string.db_articles_imageURLs)).getChildren()) {
                        imageURLs.add(imageURL.getValue(String.class));
                    }
                    for (DataSnapshot videoURL : snapshot.child(r.getString(R.string.db_articles_videoURLs)).getChildren()) {
                        videoURLs.add(videoURL.getValue(String.class));
                    }
                    boolean featured = true;

                    long timestamp =  snapshot.child(r.getString(R.string.db_articles_timestamp)).getValue(long.class);

                    article = new Article(articleID, author, title, body, category, imageURLs.toArray(new String[0]), videoURLs.toArray(new String[0]), featured, timestamp);
                    titleTextView.setText(article.getTitle());
                    if(article.getImageURLs().length != 0) { // When there are at least one article, show first image
                        ImageUtil.setImageToView(article.getImageURLs()[0], articleImage);
                    } else if(article.getVideoURLs().length != 0){
                        ImageUtil.setImageToSmallView(ImageUtil.getYoutubeThumbnail(article.getVideoURLs()[0]), articleImage);
                    }

                    ScreenUtil.setTimeToTextView(article.getTimestamp(), timeTextView);

                    boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
                    // For Finding The Correct Color and Title for Featured Articles
                    DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                            .child(r.getString(R.string.db_categories))
                            .child(article.getCategory());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "Set Color");
                            String title = snapshot.child(r.getString(R.string.db_categories_titles)).getValue(String.class);

                            int color = Color.parseColor(snapshot.child(r.getString(R.string.db_categories_color)).getValue(String.class));
                            categoryTextView.setText(title);
                            categoryTextView.setTextColor(color);
                            indicatorImageView.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
                            article.setCategoryDisplayName(title);
                            article.setCategoryDisplayColor(color);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        @Override
        public void onClick(View view) {
            Log.d(TAG, "article click");
            if(article != null)
                onArticleClick.onArticleClicked(article);
        }
    }


}


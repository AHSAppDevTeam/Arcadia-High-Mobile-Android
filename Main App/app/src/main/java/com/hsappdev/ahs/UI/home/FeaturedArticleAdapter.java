package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.List;

public class FeaturedArticleAdapter extends RecyclerView.Adapter<FeaturedArticleAdapter.FeaturedArticleViewHolder> {

    private List<String> articleIds;

    public FeaturedArticleAdapter(List<String> articles) {
        this.articleIds = articles;
    }

    @NonNull
    @Override
    public FeaturedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeaturedArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_featured_article_slide,
                        parent,
                        false
                )
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

    class FeaturedArticleViewHolder extends RecyclerView.ViewHolder {
        private Article article;
        private ConstraintLayout articleLayout;
        private ImageView articleImage;
        private Resources resources;
        private TextView titleTextView;

        public FeaturedArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.resources = itemView.getResources();
            this.articleLayout = (ConstraintLayout) itemView;
            this.articleImage = articleLayout.findViewById(R.id.featured_article_image);
            this.titleTextView = articleLayout.findViewById(R.id.featured_article_name);
        }

        public void setDetails(String articleId){
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(resources.getString(R.string.database_articles_ref))
                    .child(articleId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String author = snapshot.child("author").getValue(String.class);
                    String body = snapshot.child("body").getValue(String.class);
                    String title = snapshot.child("title").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    article = new Article(author, date, title, body);
                    titleTextView.setText(title);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void setArticleImage(int articleImage) {
            // Set clip to outline programmatically since xml is not supported due to a bug with android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.articleImage.setClipToOutline(true);
            }
            this.articleImage.setImageResource(articleImage);
        }

    }

}


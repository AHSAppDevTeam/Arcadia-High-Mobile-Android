package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
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

    public FeaturedArticleAdapter(List<String> articleIds) {
        this.articleIds = articleIds;
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
    }

    @NonNull
    @Override
    public FeaturedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeaturedArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_news_featured_article,
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

    static class FeaturedArticleViewHolder extends RecyclerView.ViewHolder {
        private Article article;
        final private ConstraintLayout articleLayout;
        final private ImageView articleImage;
        final private Resources r;
        final private TextView titleTextView;

        public FeaturedArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.r = itemView.getResources();
            this.articleLayout = (ConstraintLayout) itemView;
            this.articleImage = articleLayout.findViewById(R.id.featured_article_image);
            this.titleTextView = articleLayout.findViewById(R.id.featured_article_name);
        }

        public void setDetails(String articleId){
            DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                    .child(r.getString(R.string.db_articles))
                    .child(articleId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String author = snapshot.child(r.getString(R.string.db_articles_author)).getValue(String.class);
                    String body = snapshot.child(r.getString(R.string.db_articles_body)).getValue(String.class);
                    String title = snapshot.child(r.getString(R.string.db_articles_title)).getValue(String.class);
                    String date = snapshot.child(r.getString(R.string.db_articles_date)).getValue(String.class);
                    article = new Article(author, date, title, body);
                    titleTextView.setText(article.getTitle());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }


}


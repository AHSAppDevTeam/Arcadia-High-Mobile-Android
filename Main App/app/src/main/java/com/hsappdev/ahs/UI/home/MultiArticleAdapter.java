package com.hsappdev.ahs.UI.home;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstraction of the Medium and Small ArticleAdapter to prevent duplicate code
 * because these two adapters are very similar
 * @param <T> the type of the viewHolder
 */
public abstract class MultiArticleAdapter<T extends MultiArticleAdapter.MultiArticleViewHolder> extends RecyclerView.Adapter<T> {
    protected List<String> articleIds;
    protected OnItemClick onArticleClick;

    protected int numArticles = 2;

    public MultiArticleAdapter(List<String> articleIds, OnItemClick onArticleClick) {
        this.articleIds = articleIds;
        this.onArticleClick = onArticleClick;
    }


    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        List<String> articlesToAdd = new ArrayList<>();

        // if we are not in an odd case
        if(position < articleIds.size()/ numArticles){
            for (int index = 0; index< numArticles; index++){
                articlesToAdd.add(articleIds.get((position)* numArticles +index));
            }
        }else{
            // if we are in an odd case
            // then find the # of odd ones
            int oddOnes = articleIds.size()-(position)* numArticles;
            for (int index = 0; index<oddOnes; index++){
                articlesToAdd.add(articleIds.get((position)* numArticles +index));
            }
        }

        holder.setDetails(articlesToAdd);
    }

    @Override
    public int getItemCount() {
        if(articleIds.size() == 0){
            return 0;
        }
        if(articleIds.size()% numArticles == 0){
            return articleIds.size()/ numArticles;
        }else{
            return articleIds.size()/ numArticles +1;
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

    public abstract static class MultiArticleViewHolder extends RecyclerView.ViewHolder{
        final protected Resources r;

        final protected OnItemClick onArticleClick;

        public MultiArticleViewHolder(@NonNull View itemView, OnItemClick onArticleClick) {
            super(itemView);
            this.r = itemView.getResources();
            this.onArticleClick = onArticleClick;


        }


        public abstract void setDetails(List<String> articlesToAdd);

    }
}

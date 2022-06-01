package com.hsappdev.ahs.ui_new.reusable.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.home.CategoryViewHolder;
import com.hsappdev.ahs.UI.home.FeaturedViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AbstractDataRecyclerView<T extends > extends RecyclerView.Adapter<AbstractDataViewHolder>{
    private MutableLiv<String> dataIds;
    private int viewId = 0;

    public AbstractDataRecyclerView() {
    }

    @NonNull
    @Override
    public AbstractDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false);
            return new AbstractDataViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AbstractDataViewHolder holder, int position) {
        // TODO: check if needed the below
        // ((ViewGroup) holder.itemView).setClipChildren(false);
        // ((ViewGroup) holder.itemView).setClipToPadding(false);

        holder.setData(dataIds.get(position), onArticleClick);

    }

    @Override
    public int getItemCount() {
        return categoryIDs.size();
    }

    // GETTERS AND SETTERS

    public ArrayList<String> getDataIds() {
        return dataIds;
    }

    public void setDataIds(ArrayList<String> dataIds) {
        this.dataIds = dataIds;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }
}

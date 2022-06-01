package com.hsappdev.ahs.ui_new.reusable.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.newCache.CacheType;

import java.util.ArrayList;

public class AbstractDataRecyclerView<T extends CacheType> extends RecyclerView.Adapter<AbstractDataViewHolder<T>>{
    private ArrayList<T> dataIds;
    private int viewId;

    public AbstractDataRecyclerView() {
    }

    @NonNull
    @Override
    public AbstractDataViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false);
            return new AbstractDataViewHolder<T>(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AbstractDataViewHolder<T> holder, int position) {
        // TODO: check if needed the below
        // ((ViewGroup) holder.itemView).setClipChildren(false);
        // ((ViewGroup) holder.itemView).setClipToPadding(false);

        holder.setData(dataIds.get(position));

    }

    @Override
    public int getItemCount() {
        return dataIds.size();
    }

    // GETTERS AND SETTERS

    public ArrayList<T> getDataIds() {
        return dataIds;
    }

    public void setDataIds(ArrayList<T> dataIds) {
        this.dataIds = dataIds;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }
}

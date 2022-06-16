package com.hsappdev.ahs.ui.reusable.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.newCache.DataType;

import java.util.ArrayList;
import java.util.List;

public class AbstractDataRecyclerView<T extends DataType> extends RecyclerView.Adapter<AbstractDataViewHolder<T>>{
    private List<T> dataList = new ArrayList<>();
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
        // TODO: check if need the below
        // ((ViewGroup) holder.itemView).setClipChildren(false);
        // ((ViewGroup) holder.itemView).setClipToPadding(false);

        holder.setData(dataList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(List<T> newDataList) {

        final DataDiffCallback<T> dataDiffCallback = new DataDiffCallback<T>(this.dataList, newDataList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(dataDiffCallback);

        this.dataList.clear();
        this.dataList.addAll(newDataList);

        diffResult.dispatchUpdatesTo(this);

    }

    // GETTERS AND SETTERS

    public List<T> getDataList() {
        return dataList;
    }


    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }
}

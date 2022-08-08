package com.hsappdev.ahs.ui_new.reusable.recyclerview;

import androidx.recyclerview.widget.DiffUtil;

import com.hsappdev.ahs.newCache.DataType;

import java.util.List;

public class DataDiffCallback<T extends DataType> extends DiffUtil.Callback {
    private final List<T> oldDataList;
    private final List<T> newDataList;

    public DataDiffCallback(List<T> oldDataList, List<T> newDataList) {
        this.oldDataList = oldDataList;
        this.newDataList = newDataList;
    }

    @Override
    public int getOldListSize() {
        return oldDataList.size();
    }

    @Override
    public int getNewListSize() {
        return newDataList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDataList.get(oldItemPosition).getDataId().equals(newDataList.get(newItemPosition).getDataId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDataList.get(oldItemPosition).getDataHash().equals(newDataList.get(newItemPosition).getDataHash());
    }
}

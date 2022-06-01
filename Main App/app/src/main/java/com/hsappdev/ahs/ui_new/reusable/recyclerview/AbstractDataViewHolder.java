package com.hsappdev.ahs.ui_new.reusable.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.newCache.DataType;

public class AbstractDataViewHolder<T extends DataType> extends RecyclerView.ViewHolder{
    public AbstractDataViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public void setData(T t) {
        t.setDataToView(itemView);
    }
}

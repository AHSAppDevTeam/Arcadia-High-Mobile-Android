package com.hsappdev.ahs.ui.reusable.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.newCache.DataType;

public class AbstractDataViewHolder<T extends DataType> extends RecyclerView.ViewHolder{
    public AbstractDataViewHolder(@NonNull View itemView) {
        super(itemView);

    }

    private void init(T t) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.handleOnClick(view);
            }
        });
    }


    public void setData(T t) {
        init(t);
        t.setDataToView(itemView);
    }
}

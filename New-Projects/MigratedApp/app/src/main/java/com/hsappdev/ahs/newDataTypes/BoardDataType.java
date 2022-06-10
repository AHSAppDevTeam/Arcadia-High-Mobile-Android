package com.hsappdev.ahs.newDataTypes;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.newCache.DataType;

import java.util.List;

public class BoardDataType extends DataType {
    public List<String> articleIds;

    public long editTimestamp;

    public int sort;

    public String title;

    public BoardDataType(List<String> articleIds, long editTimestamp, int sort, String title) {
        this.articleIds = articleIds;
        this.editTimestamp = editTimestamp;
        this.sort = sort;
        this.title = title;

    }

    @Override
    public void setDataToView(View view) {
        // extract the view elements
        TextView titleTextView = view.findViewById(R.id.board_title_text);

        titleTextView.setText(title);
    }

    @Override
    public void handleOnClick(View view) {
        Toast.makeText(view.getContext(), title, Toast.LENGTH_SHORT).show();
    }
}

package com.hsappdev.ahs.UI.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hsappdev.ahs.R;

public class TextViewSelector extends androidx.appcompat.widget.AppCompatTextView implements View.OnClickListener {

    private Drawable active_bg, inactive_bg;
    private int active_textColor, inactive_textColor;
    private OnSelectionListener listener;

    public TextViewSelector(Context context) {
        super(context);
    }

    public TextViewSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextViewSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewSelector);
        active_bg = typedArray.getDrawable(R.styleable.TextViewSelector_android_background);
        inactive_bg = typedArray.getDrawable(R.styleable.TextViewSelector_backgroundInactive);
        active_textColor = typedArray.getColor(R.styleable.TextViewSelector_android_textColor,
                getResources().getColor(R.color.white, context.getTheme()));
        inactive_textColor = typedArray.getColor(R.styleable.TextViewSelector_textColorInactive,
                getResources().getColor(R.color.brightRed, context.getTheme()));
        boolean isCurrentlySelected = typedArray.getBoolean(R.styleable.TextViewSelector_selected, false);
        setSelected(isCurrentlySelected);
        typedArray.recycle();
        setOnClickListener(this);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setBackground(selected ? active_bg : inactive_bg);
        setTextColor(selected ? active_textColor : inactive_textColor);
    }

    @Override
    public void onClick(View view) {
        if (!isSelected()) {
            setSelected(true);
            if (listener != null)
                listener.onSelected();
        }
    }

    public void setOnSelectedListener(OnSelectionListener listener) {
        this.listener = listener;
    }

    interface OnSelectionListener {
        void onSelected();
    }
}

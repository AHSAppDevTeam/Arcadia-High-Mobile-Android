package com.hsappdev.ahs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hsappdev.ahs.util.ScreenUtil;

public class Adjusting_TextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final String TAG = "Adjusting_TextView";
    public Adjusting_TextView(Context context) {
        super(context);
        init(context);

    }

    public Adjusting_TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Adjusting_TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private float originalTextSize;
    public void init(Context context) {
        originalTextSize = ScreenUtil.px_to_sp(getTextSize(),context);
        //Log.d(TAG, ScreenUtil.px_to_sp(originalTextSize,context) + "sp");
        try {
            ((hello) context).addTextSizeCallback(new TextSizeCallback() {
                @Override
                public void onTextSizeChanged(float newTextScalar) {
                    setTextSize(originalTextSize*newTextScalar);
                }
            });
        } catch (ClassCastException e) {

        }
    }

    interface hello {
        void addTextSizeCallback(TextSizeCallback callback);
    }
    interface TextSizeCallback {
        void onTextSizeChanged(float newTextScalar);
    }
}

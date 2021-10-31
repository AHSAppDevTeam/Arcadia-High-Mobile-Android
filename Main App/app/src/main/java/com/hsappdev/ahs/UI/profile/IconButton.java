package com.hsappdev.ahs.UI.profile;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.hsappdev.ahs.R;

public class IconButton extends LinearLayout {

    private ImageView buttonIcon;
    private TextView buttonText;

    public IconButton(Context context, AttributeSet attrs) {
        super(context);
        // Center Content
        this.setGravity(Gravity.CENTER);
        // Get Custom and Android Attrs
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.IconButton,
                0, 0);
        String textAttr = null;
        Drawable iconAttr = null;
        Drawable backgroundAttr = null;
        try {
            textAttr = a.getString(R.styleable.IconButton_text);
            iconAttr = a.getDrawable(R.styleable.IconButton_icon);
            backgroundAttr = a.getDrawable(R.styleable.IconButton_android_background);
        } finally {
            a.recycle();
        }
        // Set Icon
        buttonIcon = new ImageView(context);
        buttonIcon.setImageDrawable(iconAttr);
        buttonText = new TextView(context);
        buttonText.setText(textAttr);
        buttonText.setTextColor(Color.WHITE);
        buttonText.setPadding((int) getResources().getDimension(R.dimen.padding_x_5), 0, 0, 0);
        // Font
        Typeface buttonFont = ResourcesCompat.getFont(context, R.font.mediumbold_lightregular);
        buttonText.setTypeface(buttonFont, Typeface.BOLD);

        int overallPadding = (int) getResources().getDimension(R.dimen.padding);
        float paddingScale = 1.5f;
        setPadding((int) (overallPadding*paddingScale), overallPadding, (int) (overallPadding*paddingScale), overallPadding);

        // Set Ripple effect
        TypedValue rippleValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, rippleValue, true);
        this.setForeground(ResourcesCompat.getDrawable(getResources(), rippleValue.resourceId, context.getTheme()));
        this.setClickable(true);
        this.setFocusable(true);
        this.setClipToOutline(true);

        this.setBackground(backgroundAttr);

        // Add Shadow Effect
        setElevation(getResources().getDimension(R.dimen.shadowElevation));

        // Add to View
        this.addView(buttonIcon);
        this.addView(buttonText);
    }

}

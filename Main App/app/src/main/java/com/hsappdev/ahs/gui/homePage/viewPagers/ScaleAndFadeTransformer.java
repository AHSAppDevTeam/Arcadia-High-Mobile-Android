package com.hsappdev.ahs.gui.homePage.viewPagers;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.hsappdev.ahs.R;

public class ScaleAndFadeTransformer implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        float curentPos = 1 - Math.abs(position);
        float maxScale = 1f;
        float minScale = 0.75f;
        float diff = maxScale-minScale;
        // Set Pivot point to center of imageView
        ImageView articleImage = page.findViewById(R.id.featured_article_image);
        int centerX = articleImage.getWidth()/2;
        int centerY = articleImage.getHeight()/2;
        page.setPivotX(centerX);
        page.setPivotY(centerY);
        // Set scale in between min and max
        page.setScaleY(minScale + diff*curentPos);
        // Fade text
        page.findViewById(R.id.featured_article_stats).setAlpha((float) Math.pow(curentPos,4));
    }
}

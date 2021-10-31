package com.hsappdev.ahs.UI.home;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.hsappdev.ahs.R;

public class ScaleAndFadeTransformer implements ViewPager2.PageTransformer {

    private final boolean isArticle;

    public ScaleAndFadeTransformer(boolean isArticle) {
        this.isArticle = isArticle;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setLayerType(View.LAYER_TYPE_NONE, null);
        float curentPos = 1 - Math.abs(position);
        float maxScale = 1f;
        float minScale = 0.75f;
        float diff = maxScale - minScale;
        // Set Pivot point to center of imageView
        int imageRef = R.id.featured_article_image;
        if (isArticle) {
            imageRef = R.id.media_image_fragment_imageView;
        }
        ImageView articleImage = page.findViewById(imageRef);
        if (articleImage == null) {
            return; // because it is a video viewer
        }
        int centerX = articleImage.getWidth() / 2;
        int centerY = articleImage.getHeight() / 2;
        page.setPivotX(centerX);
        page.setPivotY(centerY);
        // Set scale in between min and max
        page.setScaleY(minScale + diff * curentPos);
        if (!isArticle) {
            // Fade text
            page.findViewById(R.id.featured_article_stats).setAlpha((float) Math.pow(curentPos, 4));
            page.findViewById(R.id.featured_article_name).setAlpha((float) Math.pow(curentPos, 4));
        }
    }
}

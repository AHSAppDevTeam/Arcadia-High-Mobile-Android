package com.hsappdev.ahs.UI.bulletin;

import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.hsappdev.ahs.R;

public class BulletinCategoryWidget extends CardView {
    final private Resources r;
    final private String categoryId;

    final private TextView labelTextView;
    final private ImageView iconImageView;
    public BulletinCategoryWidget(@NonNull Context context, String categoryId) {
        super(context);
        this.r = getResources();
        this.categoryId = categoryId;
        View view = inflate(context, R.layout.comunity_activity_article_unit, this);
        labelTextView = view.findViewById(R.id.bulletin_icon_text);
        iconImageView = view.findViewById(R.id.bulletin_icon_imageView);
        setRadius(r.getDimension(R.dimen.padding));
    }

}

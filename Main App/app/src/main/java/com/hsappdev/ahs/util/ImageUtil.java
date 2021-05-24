package com.hsappdev.ahs.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.hsappdev.ahs.R;

public class ImageUtil {
    public static void setImageToView(String imageUrl, ImageView imageView){
        ShapeableImageView shapeableImageView = (ShapeableImageView) imageView;
        shapeableImageView.setShapeAppearanceModel(new ShapeAppearanceModel().withCornerSize(ScreenUtil.dp_to_px(imageView.getContext().getResources().getDimension(R.dimen.padding)/2, imageView.getContext())));
        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.home_img_shadow_frame)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

    }

    public static void setImageToSmallView(String imageUrl, ImageView imageView){
        ShapeableImageView shapeableImageView = (ShapeableImageView) imageView;
        shapeableImageView.setShapeAppearanceModel(new ShapeAppearanceModel().withCornerSize(ScreenUtil.dp_to_px(imageView.getContext().getResources().getDimension(R.dimen.padding)/4, imageView.getContext())));
        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.home_img_shadow_frame)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

    }
}

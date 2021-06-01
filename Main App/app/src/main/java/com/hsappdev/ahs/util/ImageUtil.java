package com.hsappdev.ahs.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.hsappdev.ahs.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

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

    public static String getYoutubeThumbnail(String videoID){
        return "https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg";
    }

    public static void setImageToZoomingView(String imageUrl, PhotoView imageView){
        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .error(R.drawable.home_img_shadow_frame)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

    }

    public static void setHeavyBlurImageToImageView(String imageUrl, ImageView imageView){
        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .transform(new BlurTransformation(100, 3), new CenterCrop())
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

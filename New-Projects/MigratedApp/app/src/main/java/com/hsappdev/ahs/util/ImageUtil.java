package com.hsappdev.ahs.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.hsappdev.ahs.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ImageUtil {
    public static void setImageToView(String imageUrl, ImageView imageView){

        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .error(R.drawable.error_img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

    }

    public static String getYoutubeThumbnail(String videoID){
        return "https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg";
    }

//    public static void setImageFullRes(String imageUrl, PhotoView imageView){
//        Glide
//                .with(imageView.getContext())
//                .load(imageUrl)
//                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .error(R.drawable.error_img)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageView);
//
//    }

    public static void setHeavyBlurImageToImageView(String imageUrl, ImageView imageView){
        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .transform(new BlurTransformation(100, 3), new CenterCrop())
                .error(R.drawable.error_img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);


    }

}

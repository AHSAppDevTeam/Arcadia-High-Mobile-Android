package com.hsappdev.ahs.util;

import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.hsappdev.ahs.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ImageUtil {
    public static void setImageToView(String imageUrl, ImageView imageView){
        if (imageView instanceof ShapeableImageView) {
            ShapeableImageView shapeableImageView = (ShapeableImageView) imageView;
            shapeableImageView.setShapeAppearanceModel(new ShapeAppearanceModel().withCornerSize(ScreenUtil.dp_to_px(imageView.getContext().getResources().getDimension(R.dimen.padding) / 2, imageView.getContext())));
        }

        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .error(R.drawable.img_frame_large)
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
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.img_frame_large)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

    }

    public static void setHeavyBlurImageToImageView(String imageUrl, ImageView imageView){
        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .transform(new BlurTransformation(100, 3), new CenterCrop())
                .error(R.drawable.img_frame_large)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);


    }

    /**
     * Deprecated
     * use setImageToView for consistency
     * @param imageUrl
     * @param imageView
     */
    @Deprecated
    public static void setImageToSmallView(String imageUrl, ImageView imageView){
        setImageToView(imageUrl, imageView);
//        ShapeableImageView shapeableImageView = (ShapeableImageView) imageView;
//        shapeableImageView.setShapeAppearanceModel(new ShapeAppearanceModel().withCornerSize(ScreenUtil.dp_to_px(imageView.getContext().getResources().getDimension(R.dimen.padding)/4, imageView.getContext())));
//        Glide
//                .with(imageView.getContext())
//                .load(imageUrl)
//                .centerCrop()
//                .error(R.drawable.img_frame_large)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageView);

    }

    public static void setCircleImageToView(String imageUrl, ImageView imageView) {
        if (imageView instanceof ShapeableImageView) {
            ShapeableImageView shapeableImageView = (ShapeableImageView) imageView;
            shapeableImageView.setShapeAppearanceModel(new ShapeAppearanceModel().withCornerSize(ScreenUtil.dp_to_px(imageView.getContext().getResources().getDimension(R.dimen.padding) / 2, imageView.getContext())));
        }
        Glide
                .with(imageView.getContext())
                .load(imageUrl)
                .circleCrop()
                .error(R.drawable.img_frame_large)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}

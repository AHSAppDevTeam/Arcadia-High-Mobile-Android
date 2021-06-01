package com.hsappdev.ahs.mediaPager;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.ImageUtil;

public class ImageViewActivity extends AppCompatActivity {

    public static final String IMAGE_URL_KEY = "imageURLKEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_activity);

        String url = getIntent().getStringExtra(IMAGE_URL_KEY);

        ImageUtil.setImageToZoomingView(url, findViewById(R.id.zooming_photo_view));

        ImageUtil.setHeavyBlurImageToImageView(url, findViewById(R.id.zooming_photo_view_background_img));

        findViewById(R.id.media_image_exit_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
}

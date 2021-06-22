package com.hsappdev.ahs.mediaPager;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.ImageUtil;

public class ImageFragment extends Fragment implements View.OnClickListener{

    private Media media;


    public ImageFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String url = getArguments().getString(ImageViewActivity.IMAGE_URL_KEY);
        this.media = new Media(url, false);
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        ImageView image = view.findViewById(R.id.media_image_fragment_imageView);
        ImageUtil.setImageToView(media.getMediaURL(), image);

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        image.setForeground(getResources().getDrawable(outValue.resourceId, getContext().getTheme()));
        image.setClickable(true);
        image.setFocusable(true);

        image.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        ((OnImageClick) getActivity()).onImageClick(media);
    }
}

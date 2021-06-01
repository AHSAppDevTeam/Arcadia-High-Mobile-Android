package com.hsappdev.ahs.mediaPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        ImageUtil.setImageToView(media.getMediaURL(), view.findViewById(R.id.media_image_fragment_imageView));

        view.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        ((OnImageClick) getActivity()).onImageClick(media);
    }
}

package com.example.youtubetest;

import java.util.ArrayList;
import java.util.List;

public class YoutubeVideoCallback<T extends YoutubeVideoCallback.YoutubeVideoInterface> {
    private List<T> registeredViews;

    public YoutubeVideoCallback() {
        this.registeredViews = new ArrayList<>();
    }

    public void registerView(T viewHolder){
        registeredViews.add(viewHolder);
    }

    public void releaseAll(){
        for (T viewHolder :
                registeredViews) {
            viewHolder.releaseVideo();
        }
    }


    public interface YoutubeVideoInterface {
        void releaseVideo();
        void initVideo();
    }
}

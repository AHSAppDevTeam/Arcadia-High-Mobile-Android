package com.hsappdev.ahs.mediaPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

public class YouTubeFragment extends YouTubePlayerSupportFragmentX implements YoutubeVideoCallback.YoutubeVideoInterface, YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener {
    private static final String TAG = "MediaFragment";
    private Media media;

    YouTubePlayer youTubePlayer;

    private int lastPlayTime = 0;

    private YoutubeVideoCallback<YouTubeFragment> youtubeVideoCallback;

    public YouTubeFragment() {
        super();
    }

    public YouTubeFragment(Media media, YoutubeVideoCallback<YouTubeFragment> youtubeVideoCallback) {
        super();
        this.media = media;
        this.youtubeVideoCallback = youtubeVideoCallback;
        this.youtubeVideoCallback.registerView(this);

    }

    @Override
    public void releaseVideo() {
        if(youTubePlayer != null) {
            youTubePlayer.release();
        }
    }

    @Override
    public void initVideo() {
        initialize("<<API KEY>>", this);
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.cueVideo(media.getMediaURL(), lastPlayTime);
        youTubePlayer.setOnFullscreenListener(this);
        Log.d(TAG, "onInitializationSuccess: ");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onFullscreen(boolean b) {

    }
}

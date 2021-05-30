package com.example.youtubetest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.google.android.youtube.player.YouTubePlayerView;

public class MediaFragment extends YouTubePlayerSupportFragmentX implements YoutubeVideoCallback.YoutubeVideoInterface, YouTubePlayer.OnInitializedListener {
    private static final String TAG = "MediaFragment";
    private Media media;

    YouTubePlayer youTubePlayer;

    private int lastPlayTime = 0;

    private YoutubeVideoCallback<MediaFragment> youtubeVideoCallback;

    public MediaFragment() {
        super();
    }

    public MediaFragment(Media media, YoutubeVideoCallback<MediaFragment> youtubeVideoCallback) {
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
        Log.d(TAG, "onInitializationSuccess: ");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}

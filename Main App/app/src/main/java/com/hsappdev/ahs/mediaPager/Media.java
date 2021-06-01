package com.hsappdev.ahs.mediaPager;

public class Media {
    private boolean isVideo;
    private String mediaURL;

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Media(String mediaURL, boolean isVideo) {
        this.mediaURL = mediaURL;
        this.isVideo = isVideo;
    }
}

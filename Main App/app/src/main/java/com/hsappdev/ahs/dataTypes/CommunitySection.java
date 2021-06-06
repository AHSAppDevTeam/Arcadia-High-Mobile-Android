package com.hsappdev.ahs.dataTypes;

import android.os.Parcel;
import android.os.Parcelable;

public class CommunitySection implements Parcelable {
    private String categoryDisplayName, blurb, displayColor;
    private boolean isFeatured;
    private String[] thumbURLs;

    public CommunitySection(String categoryDisplayName, String blurb, String displayColor, boolean isFeatured, String[] thumbURLs) {
        this.categoryDisplayName = categoryDisplayName;
        this.blurb = blurb;
        this.displayColor = displayColor;
        this.isFeatured = isFeatured;
        this.thumbURLs = thumbURLs;
    }

    // GETTERS AND SETTERS
    public String getCategoryDisplayName() {
        return categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this.categoryDisplayName = categoryDisplayName;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public String[] getThumbURLs() {
        return thumbURLs;
    }

    public void setThumbURLs(String[] thumbURLs) {
        this.thumbURLs = thumbURLs;
    }

    protected CommunitySection(Parcel in) {
        categoryDisplayName = in.readString();
        blurb = in.readString();
        displayColor = in.readString();
        isFeatured = in.readByte() != 0;
        thumbURLs = in.createStringArray();
    }

    public static final Creator<CommunitySection> CREATOR = new Creator<CommunitySection>() {
        @Override
        public CommunitySection createFromParcel(Parcel in) {
            return new CommunitySection(in);
        }

        @Override
        public CommunitySection[] newArray(int size) {
            return new CommunitySection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryDisplayName);
        dest.writeString(blurb);
        dest.writeString(displayColor);
        dest.writeByte((byte) (isFeatured ? 1 : 0));
        dest.writeStringArray(thumbURLs);
    }
}

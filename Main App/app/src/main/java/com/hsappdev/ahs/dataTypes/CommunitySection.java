package com.hsappdev.ahs.dataTypes;

import android.os.Parcel;
import android.os.Parcelable;

public class CommunitySection implements Parcelable {
    private String categoryId, categoryDisplayName, blurb;
    private int displayColor;
    private boolean isFeatured;
    private String[] thumbURLs;

    public CommunitySection(String categoryId, String categoryDisplayName, String blurb, int displayColor, boolean isFeatured) {
        this.categoryId = categoryId;
        this.categoryDisplayName = categoryDisplayName;
        this.blurb = blurb;
        this.displayColor = displayColor;
        this.isFeatured = isFeatured;
    }

    public CommunitySection(String categoryId, String title, String blurb, int displayColor, boolean isFeatured, String[] thumbURLs) {
        this(categoryId, title, blurb, displayColor, isFeatured);
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

    public int getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(int displayColor) {
        this.displayColor = displayColor;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String[] getThumbURLs() {
        return thumbURLs;
    }

    public void setThumbURLs(String[] thumbURLs) {
        this.thumbURLs = thumbURLs;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    protected CommunitySection(Parcel in) {
        categoryId = in.readString();
        categoryDisplayName = in.readString();
        blurb = in.readString();
        displayColor = in.readInt();
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
        dest.writeString(categoryId);
        dest.writeString(categoryDisplayName);
        dest.writeString(blurb);
        dest.writeInt(displayColor);
        dest.writeByte((byte) (isFeatured ? 1 : 0));
        dest.writeStringArray(thumbURLs);
    }
}

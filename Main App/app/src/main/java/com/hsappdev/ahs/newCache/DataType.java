package com.hsappdev.ahs.newCache;

import android.view.View;

import androidx.room.Ignore;

public abstract class DataType {

    /**
     * A unique string id for each data type
     */
    @Ignore
    public String dataId;

    /**
     * Hash of the contents inside this object (to check if contents are the same)
     */
    @Ignore
    public String dataHash;

    /**
     * Shows if this article has been loaded yet<br>
     * true = not loaded<br>
     * false = loaded
     */
    @Ignore
    public boolean isLoading = false;


    // METHODS TO DISPLAY THE DATA

//    /**
//     * Resource id (int) for the view to be displayed when used in recycler view
//     */
//    public static int displayResourceViewId = 0;

    /**
     * Set data to the view, using a overridden method
     */
    public abstract void setDataToView(View view);

    // GETTERS AND SETTERS


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public abstract void handleOnClick(View view);

//    public static int getDisplayResourceViewId() {
//        return displayResourceViewId;
//    }
//
//    public static void setDisplayResourceViewId(int displayResourceViewId) {
//        DataType.displayResourceViewId = displayResourceViewId;
//    }
}

package com.hsappdev.ahs.newCache;

import android.view.View;

public abstract class CacheType {

    /**
     * Shows if this article has been loaded yet<br>
     * true = not loaded<br>
     * false = loaded
     */
    public boolean isLoading = false;


    // METHODS TO DISPLAY THE DATA

    /**
     * Resource id (int) for the view to be displayed when used in recycler view
     */
    public static final int displayResourceViewId = 0;

    /**
     * Set data to the view, using a overridden method
     */
    public abstract void setDataToView(View view);
}

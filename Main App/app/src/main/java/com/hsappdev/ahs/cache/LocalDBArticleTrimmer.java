package com.hsappdev.ahs.cache;

import android.content.res.Resources;
import android.os.Process;
import android.util.Log;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalDBArticleTrimmer implements Runnable, OnCategoryLoadedCallback{
    private static final String TAG = "LocalDBArticleTrimmer";
    private final Resources r;

    public LocalDBArticleTrimmer(Resources r) {
        this.r = r;
    }

    @Override
    public void run() {
//        Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
//        Log.d(TAG, "run: start db article trimming");
//        CategoryLoader.getInstance().getCategory(r.getString(R.string.db_location_ausdNews), r, this);
//        CategoryLoader.getInstance().getCategory(r.getString(R.string.db_location_bulletin), r, this);
//        CategoryLoader.getInstance().getCategory(r.getString(R.string.db_location_community), r, this);
    }

    @Override
    public void onCategoryLoaded(Category category) {
        //List<String> article = category.getArticleIds();
        
    }
}

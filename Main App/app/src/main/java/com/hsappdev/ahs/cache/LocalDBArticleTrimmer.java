package com.hsappdev.ahs.cache;

import android.os.Process;
import android.util.Log;

public class LocalDBArticleTrimmer implements Runnable{
    private static final String TAG = "LocalDBArticleTrimmer";
    
    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
        Log.d(TAG, "run: start db article trimming");

    }
}

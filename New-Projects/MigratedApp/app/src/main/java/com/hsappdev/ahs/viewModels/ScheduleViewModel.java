package com.hsappdev.ahs.viewModels;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.hsappdev.ahs.newDataTypes.WeekData;

import java.util.HashMap;

public class ScheduleViewModel extends ViewModel {

    private static final String TAG = "ScheduleFragmentViewMod";

    private final HashMap<Integer, WeekData> weeks = new HashMap<>();


    public HashMap<Integer, WeekData> getCalendarData() {
        return weeks;
    }

    public ScheduleViewModel() {


    }

    public void start(Resources r) {
        startLoadingScheduleData(r);
    }

    private void startLoadingScheduleData(Resources r) {
        // populate with empty week objects
        // assume 54 weeks
        for (int i = 1; i <= 54; i++) { // start at 1
            weeks.put(i, new WeekData("w"+i, r));
        }
    }
}

package com.hsappdev.ahs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsappdev.ahs.newDataTypes.DayData;
import com.hsappdev.ahs.newDataTypes.ScheduleData;
import com.hsappdev.ahs.newDataTypes.WeekData;
import com.hsappdev.ahs.viewModels.ScheduleViewModel;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private static final String TAG = "ScheduleFragment";

    private ScheduleViewModel viewModel;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the view model shared between the classes
        viewModel = new ViewModelProvider(requireActivity()).get(ScheduleViewModel.class);

        // TODO: testing only

        HashMap<Integer, WeekData> weeks = viewModel.getCalendarData();

        for (int i = 1; i <= 54; i++) {
            WeekData week = weeks.get(i);
            if (week != null) {
                for (int j = 0; j < 7; j++) {
                    int finalI = i;
                    int finalJ = j;

                    if(week.getDayList().get(j) != null) {
                        week.getDayList().get(j).observe(requireActivity(), new Observer<DayData>() {
                            @Override
                            public void onChanged(DayData dayData) {
                                String scheduleId = dayData.getScheduleId();
                                Log.d(TAG, String.format("week: %d, day: %d, : %s", finalI, finalJ, scheduleId));

                                // continue
                                dayData.getSchedule().observe(requireActivity(), new Observer<ScheduleData>() {
                                    @Override
                                    public void onChanged(ScheduleData scheduleData) {
                                        Log.d(TAG, scheduleData.toString());
                                    }
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, String.format("null at week: %d, day: %d", finalI, finalJ));

                    }
                }

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}
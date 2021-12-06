package com.hsappdev.ahs.UI.profile;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hsappdev.ahs.AboutUsActivity;
import com.hsappdev.ahs.CalendarActivity;
import com.hsappdev.ahs.NotificationSettingsActivity;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.SettingsManager;
import com.hsappdev.ahs.TermsAndAgreementsActivity;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarDayLoadCallback;
import com.hsappdev.ahs.UI.calendar.calendarBackend.CalendarScheduleLoadCallback;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Day;
import com.hsappdev.ahs.UI.calendar.calendarBackend.Schedule;
import com.hsappdev.ahs.UI.calendar.newCalendar.CalendarBackendNew;
import com.hsappdev.ahs.util.Helper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private ProfileViewModel mViewModel;

    private ProfileCardFragment profileCardFragment;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    private SettingsManager.DayNightCallback dayNightCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dayNightCallback = (SettingsManager.DayNightCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        initProfileCardFragment(view);
        // Header
        TextView profileHeader = view.findViewById(R.id.profile_header);
        String[] headerSplit = profileHeader.getText().toString().split(" ");
        Helper.setBoldRegularText(profileHeader, headerSplit[0], " " + headerSplit[1]);


        SwitchMaterial dayNightSwitch = view.findViewById(R.id.profile_theme_mode_switch);
        SettingsManager settingsManager = SettingsManager.getInstance(getActivity().getApplicationContext());
        Log.d(TAG, "Current dayNight state on Open: " + settingsManager.isNightModeOn());
        dayNightSwitch.setChecked(settingsManager.isNightModeOn());
        dayNightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d(TAG, "New dayNight state: " + b);
            settingsManager.updateNightMode(b);
            dayNightCallback.onNewMode(b);
        });

        LinearLayout notificationSettingBtn = view.findViewById(R.id.profile_notifications_btn);
        notificationSettingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
            startActivity(intent);
        });

        LinearLayout calendarBtn = view.findViewById(R.id.profile_calendar_btn);
        calendarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CalendarActivity.class);
            startActivity(intent);
        });

        LinearLayout termsBtn = view.findViewById(R.id.profile_terms_btn);
        termsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TermsAndAgreementsActivity.class);
            startActivity(intent);
        });

        LinearLayout aboutUsBtn = view.findViewById(R.id.profile_about_us_btn);
        aboutUsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(intent);
        });

        // Calendar Button
        CalendarBackendNew calendarBackend = CalendarBackendNew.getInstance();
        // View findByIds here
        TextView schedulePeriod = view.findViewById(R.id.profile_schedule_period);
        TextView timeRemaining = view.findViewById(R.id.profile_schedule_time_remaining);
        TextView timeRemainingHeader = view.findViewById(R.id.profile_schedule_time_remaining_header);
        Date initialTime = Calendar.getInstance().getTime();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Calendar currentTime = Calendar.getInstance();
                LocalDate localDate = LocalDate.now(); // local date is more accurate for week of year and day of week
                //get time in hours and minutes(basically 24 hour time format)
                int hours = currentTime.get(Calendar.HOUR_OF_DAY);
                int minutes = currentTime.get(Calendar.MINUTE);
                //convert hours and minutes to total minutes after midnight
                int currentTotalMinutes = hours * 60 + minutes;

                //Note: I made this just to test how timer would work when updating the view, it kind've crashes the app whenever i test it so
                String currentTimeRemaining = Integer.toString(currentTotalMinutes);//test, don't use in final code

                //TODO use currentTotalMinutes and compare with Schedule.periodIDs and Schedule.timestamps to display period and time

                // Callback
                calendarBackend.registerForCallback(getWeekOfYear(localDate), localDate.getDayOfWeek().getValue(), new CalendarDayLoadCallback() {
                    @Override
                    public void onCalendarDayLoad(Day requestedDay) {
                        if(getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update view code
                                // Process the data
                                if(requestedDay.getScheduleId().equals("weekend")){

                                    schedulePeriod.setText("Weekend");
                                    timeRemainingHeader.setVisibility(View.GONE);
                                    timeRemaining.setText("");
                                } else {

                                    // Load the schedule
                                    requestedDay.loadSchedule(new CalendarScheduleLoadCallback() {
                                        @Override
                                        public void onCalendarScheduleLoad(Schedule schedule) {
                                            if(!schedule.getTimestamps().isEmpty()) {
                                                int lastTimestamp = schedule.getTimestamps().get(schedule.getTimestamps().size() - 1);
                                                int firstTimestamp = schedule.getTimestamps().get(0);
                                                if (currentTotalMinutes >= lastTimestamp) {
                                                    // after school
                                                    schedulePeriod.setText("After School");
                                                    timeRemainingHeader.setVisibility(View.GONE);
                                                    timeRemaining.setText("");
                                                } else if (currentTotalMinutes < firstTimestamp) {
                                                    // before school
                                                    schedulePeriod.setText("Before School");
                                                    timeRemainingHeader.setVisibility(View.GONE);
                                                    timeRemaining.setText("");
                                                }

                                                // check where we are at by looping
                                                for (int i = 0; i < schedule.getTimestamps().size(); i+=2) {
                                                    int timestampStart = schedule.getTimestamps().get(i);
                                                    int timestampEnd = schedule.getTimestamps().get(i+1);
                                                    int periodNum = Integer.parseInt(schedule.getPeriodIDs().get(i));
                                                    int passingPeriodEnd = timestampEnd;
                                                    if(i+1 < schedule.getPeriodIDs().size()) {
                                                        passingPeriodEnd =  schedule.getTimestamps().get(i+2);
                                                    }


                                                        // timestampStart - timestampEnd is a period
                                                    // timestampEnd - passingPeriodEnd is a passing period

                                                    if(currentTotalMinutes >= timestampStart && currentTotalMinutes < timestampEnd) {
                                                        // we are in period periodNum
                                                        schedulePeriod.setText(periodNum + " Period");
                                                        timeRemainingHeader.setVisibility(View.VISIBLE);
                                                        timeRemaining.setText((timestampEnd - currentTotalMinutes) + " minutes");
                                                    } else if(currentTotalMinutes >= timestampEnd && currentTotalMinutes < passingPeriodEnd) {
                                                        schedulePeriod.setText("Passing");
                                                        timeRemainingHeader.setVisibility(View.VISIBLE);
                                                        timeRemaining.setText((passingPeriodEnd - currentTotalMinutes) + " minutes");
                                                    }

                                                }
                                            }
                                        }
                                    });
                                }

                            }
                        });
                    }

                    @Override
                    public int getRequestedDate() {
                        return localDate.getDayOfWeek().getValue(); // Change this to dayNumber (the day of week)
                    }
                });
            }
        };

        timer.schedule(timerTask, initialTime, 1000);




        return view;
    }

    // Helper
    private int getWeekOfYear(LocalDate date) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        Log.d(TAG, "getWeekOfYear: " + LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private void initProfileCardFragment(View view) {
        profileCardFragment = new ProfileCardFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.profileCardFragmentHolder, profileCardFragment)
                .commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

}
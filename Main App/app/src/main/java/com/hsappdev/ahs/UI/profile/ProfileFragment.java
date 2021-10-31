package com.hsappdev.ahs.UI.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hsappdev.ahs.util.Helper;

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
        return view;
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
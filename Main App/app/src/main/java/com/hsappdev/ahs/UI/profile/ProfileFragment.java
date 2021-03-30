package com.hsappdev.ahs.UI.profile;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hsappdev.ahs.BottomNavigationCallback;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.SettingsManager;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private ProfileViewModel mViewModel;

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
        SwitchMaterial dayNightSwitch = view.findViewById(R.id.profile_dayNight_switch);
        SettingsManager settingsManager = SettingsManager.getInstance(getActivity().getApplicationContext());
        Log.d(TAG, "Current dayNight state on Open: " + settingsManager.isNightModeOn());
        dayNightSwitch.setChecked(settingsManager.isNightModeOn());
        dayNightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d(TAG, "New dayNight state: " + b);
            settingsManager.updateNightMode(b);
            dayNightCallback.onNewMode(b);
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}
package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hsappdev.ahs.firebaseMessaging.NotificationSetup;
import com.hsappdev.ahs.util.Helper;

public class NotificationSettingsActivity extends AppCompatActivity {
    LinearLayout controlLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        // Header
        TextView profileHeader = findViewById(R.id.notification_settings_header);
        String[] headerSplit = profileHeader.getText().toString().split(" ");
        Helper.setBoldRegularText(profileHeader, headerSplit[0], " " + headerSplit[1]);

        controlLayout = findViewById(R.id.notification_settings_controls);
        // Add controls

        TextView home = new TextView(this),
                community = new TextView(this),
                bulletin = new TextView(this);
        int size = 28;
        home.setText("Home");
        home.setTextSize(size);
        community.setText("Community");
        community.setTextSize(size);
        bulletin.setText("Bulletin");
        bulletin.setTextSize(size);

        controlLayout.addView(home);
        for(String channel : NotificationSetup.homeChannels) {
            setUpChannelControl(channel, controlLayout);
        }
        controlLayout.addView(community);
        for(String channel : NotificationSetup.communityChannels) {
            setUpChannelControl(channel, controlLayout);
        }
        controlLayout.addView(bulletin);
        for(String channel : NotificationSetup.bulletinChannels) {
            setUpChannelControl(channel, controlLayout);
        }
    }

    private void setUpChannelControl(String channel, LinearLayout controlLayout) {
        View view = getLayoutInflater().inflate(R.layout.activity_notification_settings_control, null, false);
        TextView label = view.findViewById(R.id.notification_settings_control_text);
        label.setText(channel);
        SwitchMaterial control = view.findViewById(R.id.notification_settings_control_switch);
        control.setChecked(NotificationSetup.getIfChannelIsEnabled(channel, this));
        control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NotificationSetup.setIfChannelIsEnabled(channel, isChecked, NotificationSettingsActivity.this);
                if(isChecked) {
                    NotificationSetup.subscribe(NotificationSettingsActivity.this, channel);
                } else {
                    NotificationSetup.unsubscribe(NotificationSettingsActivity.this, channel);
                }
            }
        });

        controlLayout.addView(view);
    }
}
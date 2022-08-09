package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hsappdev.ahs.UI.profile.ProfileCardFragment;

public class NfcCardActivity extends AppCompatActivity {

    private ProfileCardFragment profileCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_card);

        profileCardFragment = new ProfileCardFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profileCardFragmentHolder, profileCardFragment)
                .commit();
    }
}
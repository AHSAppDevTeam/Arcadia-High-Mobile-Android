package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;

import com.hsappdev.ahs.UI.nfcCard.NfcCardModalFragment;
import com.hsappdev.ahs.UI.profile.ProfileCardFragment;

public class NfcCardActivity extends AppCompatActivity {

    private ProfileCardFragment profileCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_nfc_card);

        NfcCardModalFragment modalBottomSheet = new NfcCardModalFragment();
        modalBottomSheet.show(getSupportFragmentManager(), NfcCardModalFragment.TAG);

    }
}

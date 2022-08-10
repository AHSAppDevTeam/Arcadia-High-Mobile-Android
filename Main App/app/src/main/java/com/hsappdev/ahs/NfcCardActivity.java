package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hsappdev.ahs.UI.nfcCard.NfcCardModalFragment;
import com.hsappdev.ahs.UI.profile.ProfileCardFragment;
import com.nfc.NfcHandlerActivity;

public class NfcCardActivity extends NfcHandlerActivity {

    private NfcCardModalFragment modalBottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_nfc_card);

        modalBottomSheet = new NfcCardModalFragment();
        modalBottomSheet.show(getSupportFragmentManager(), NfcCardModalFragment.TAG);


    }

    @Override
    public void onNfcFail() {
        modalBottomSheet.onNfcFail();
    }

    @Override
    public void onNfcSuccess() {
        modalBottomSheet.onNfcSuccess();

    }
}

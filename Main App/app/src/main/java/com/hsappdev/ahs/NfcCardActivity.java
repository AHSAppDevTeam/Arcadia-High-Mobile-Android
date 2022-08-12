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

        modalBottomSheet = new NfcCardModalFragment(nfcStatusCode);
        modalBottomSheet.show(getSupportFragmentManager(), NfcCardModalFragment.TAG);

        if(nfcStatusCode == 1) {
            modalBottomSheet.onNfcSuccess();
        } else if (nfcStatusCode == -1) {
            modalBottomSheet.onNfcFail();
        }


    }

    @Override
    public void onNfcFail() {
        if(modalBottomSheet != null) {
            modalBottomSheet.onNfcFail();
        }
    }

    @Override
    public void onNfcSuccess() {
        if(modalBottomSheet != null) {
            modalBottomSheet.onNfcSuccess();
        }
    }
}

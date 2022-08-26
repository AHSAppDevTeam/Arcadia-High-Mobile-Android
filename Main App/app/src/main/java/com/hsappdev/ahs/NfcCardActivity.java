package com.hsappdev.ahs;

import android.os.Bundle;

import com.hsappdev.ahs.UI.nfcCard.NfcCardModalFragment;
import com.hsappdev.ahs.nfc.NfcHandlerActivity;

public class NfcCardActivity extends NfcHandlerActivity {

    private NfcCardModalFragment modalBottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modalBottomSheet = new NfcCardModalFragment(nfcStatusCode, isNfcSupported());
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

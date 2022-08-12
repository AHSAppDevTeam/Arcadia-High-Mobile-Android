package com.hsappdev.ahs.UI.nfcCard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.profile.ProfileCardFragment;

public class NfcCardModalFragment extends BottomSheetDialogFragment {

    public static final String TAG = "NfcCardModalFragment";
    private ProfileCardFragment profileCardFragment;

    private TextView userMessage;
    private ImageView nfcStatus;

    private int nfcStatusCode = 0; // 0  nothing, 1 good, -1 bad

    public NfcCardModalFragment() {

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(getActivity() != null) getActivity().finish();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG, "onCancel: " + getActivity());
        if(getActivity() != null) getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nfc_card_modal_fragment, container, false);

        profileCardFragment = new ProfileCardFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.profileCardFragmentHolder, profileCardFragment)
                .commit();

        Log.d(TAG, "View: " + view.toString());
        Log.d(TAG, "TextviewB: " + userMessage);

        userMessage = view.findViewById(R.id.nfc_card_modal_message);
        userMessage.setText("Place your phone over the reader.");
        Log.d(TAG, "TextviewA: " + userMessage);

        nfcStatus = view.findViewById(R.id.nfc_card_status_icon);
        nfcStatus.setVisibility(View.INVISIBLE);

        if(nfcStatusCode == 1) renderSuccess();
        if(nfcStatusCode == -1) renderFail();


        return view;
    }


    public void onNfcSuccess() {
        nfcStatusCode = 1;
        if(getView() == null) return;

        renderSuccess();
    }

    public void onNfcFail() {
        nfcStatusCode = -1;
        if(getView() == null) return;

        renderFail();

    }

    private void renderFail(){
        Log.d(TAG, "TextviewC: " + userMessage);
        userMessage.setText("Failed to send ID, try reopening the app");
        nfcStatus.setVisibility(View.VISIBLE);
        nfcStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_nfc_error));
        vibratePhone();
    }

    private void renderSuccess(){
        Log.d(TAG, "TextviewC: " + userMessage);
        userMessage.setText("Student ID Sent!");
        nfcStatus.setVisibility(View.VISIBLE);
        nfcStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_nfc_check_circle));
        vibratePhone();
    }

    private void vibratePhone(){
        if(getActivity() != null) {
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // deprecated in API 26
                v.vibrate(250);
            }
        }
    }
}

package com.hsappdev.ahs.UI.nfcCard;

import android.content.DialogInterface;
import android.os.Bundle;
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

        userMessage = view.findViewById(R.id.nfc_card_modal_message);
        userMessage.setText("Sending Student ID...");

        nfcStatus = view.findViewById(R.id.nfc_card_status_icon);
        nfcStatus.setVisibility(View.INVISIBLE);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.profileCardFragmentHolder, profileCardFragment)
                .commit();

        return view;
    }


    public void onNfcSuccess() {
        userMessage.setText("Student ID Sent!");
        nfcStatus.setVisibility(View.VISIBLE);
        nfcStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_nfc_check_circle));
    }

    public void onNfcFail() {
        userMessage.setText("Failed to send ID, try reopening the app");
        nfcStatus.setVisibility(View.VISIBLE);
        nfcStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_nfc_error));
    }
}

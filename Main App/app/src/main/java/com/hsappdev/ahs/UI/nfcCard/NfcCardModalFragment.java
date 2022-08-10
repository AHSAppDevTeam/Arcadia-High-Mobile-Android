package com.hsappdev.ahs.UI.nfcCard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.UI.profile.ProfileCardFragment;

public class NfcCardModalFragment extends BottomSheetDialogFragment {

    public static final String TAG = "NfcCardModalFragment";
    private ProfileCardFragment profileCardFragment;

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

        return view;
    }


}

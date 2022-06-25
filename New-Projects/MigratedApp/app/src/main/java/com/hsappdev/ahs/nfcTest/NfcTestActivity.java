package com.hsappdev.ahs.nfcTest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.hsappdev.ahs.R;

public class NfcTestActivity extends AppCompatActivity {

    private static final String TAG = "NfcTestActivity";

    private IntentFilter[] intentFiltersArray = null;

    private NfcAdapter nfcAdapter;

    private PendingIntent pendingIntent;
    private String[][] techListsArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_test);

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        intentFiltersArray = new IntentFilter[] {ndef, };
        techListsArray = new String[][] { new String[] { Ndef.class.getName() } };


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        nfcAdapter.disableForegroundDispatch(this);

    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "newIntent");


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Log.d(TAG, "newIntent1");

            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                    for (NdefRecord record : messages[i].getRecords()) {
                        String msg = new String(record.getPayload());
                        Log.d(TAG, msg);
                    }

                }
            }
        }
    }
}
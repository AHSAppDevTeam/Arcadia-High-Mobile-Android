package com.hsappdev.ahs.nfc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.HashUtil;


import java.io.IOException;
import java.util.Arrays;

/**
 * An activity that contains nfc features built-in
 */
public abstract class NfcHandlerActivity extends AppCompatActivity implements NfcResultHandler {

    public static final String SHARED_PREF_ID = "NFC_DATA";

    public static final String SHARED_PREF_ID_DATA_KEY = "ID_NUMBER";

    protected int nfcStatusCode = 0; // 0  nothing, 1 good, -1 bad

    private static final String TAG = "NfcHandlerActivity";

    private PendingIntent pendingIntent;

    private IntentFilter[] intentFiltersArray = null;

    private NfcAdapter nfcAdapter;
    private boolean isNfcSupported = false;
    private boolean isUserSignedIn = false;


    /**
     * Make sure to call super when overriding
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());

        // check for nfc support
        NfcManager manager = (NfcManager) getApplicationContext().getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            // supports NFC
            isNfcSupported = true;
        }

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        intentFiltersArray = new IntentFilter[]{ndef,};
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(nfcAdapter != null && isNfcSupported)
//            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //if(nfcAdapter != null && isNfcSupported)
        //    nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // nfcAdapter.disableForegroundDispatch(this);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            if(!isUserSignedIn()) return;

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (Arrays.asList(tag.getTechList()).contains(Ndef.class.getName())) { // ensure NDEF
                Ndef ndef = Ndef.get(tag);

                if (ndef != null) {
                    // tag is for sure ndef
                    try {

                        int idNumber = readStudentIdNumber()*10;

                        if(!isUserSignedIn) return; // additional safeguard

                        byte[] serializedData = new byte[5];

                        // create serializedData
                        serializedData[4] = (byte) (idNumber);
                        serializedData[3] = (byte) ((idNumber >> 8) );
                        serializedData[2] = (byte) ((idNumber >> 16) );
                        serializedData[1] = (byte) 0x00;
                        serializedData[0] = (byte) 0xCD;



                        for (int i = 0; i < serializedData.length; i++) {
                            Log.d(TAG, serializedData[i] + "");
                        }

                        int[] passwordIntegers = getResources().getIntArray(R.array.secret_salt); //getResources().getString(R.string.super_secret_nfc_salt).getBytes(StandardCharsets.US_ASCII);

                        byte[] password = new byte[passwordIntegers.length];

                        for (int i = 0; i < passwordIntegers.length; i++) {
                            password[i] = (byte) passwordIntegers[i];
                        }

                        byte[] hash = HashUtil.getSha256Hash(serializedData, password);

                        NdefRecord ndefRecordHash = NdefRecord.createExternal("a", "h", Arrays.copyOfRange(hash, 0, 16));
                        NdefRecord ndefRecordData = NdefRecord.createExternal("a", "d", serializedData);

                        NdefMessage ndefMessage = new NdefMessage(ndefRecordHash, ndefRecordData);

                        ndef.connect();
                        Log.d(TAG, "Connected");
                        if (ndef.isWritable()) {
                            ndef.writeNdefMessage(ndefMessage);
                            nfcStatusCode = 1;
                            onNfcSuccess();
                        } else {
                            Log.d(TAG, "Not writable!");
                            nfcStatusCode = -1;
                            onNfcFail();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // tag failed to write
                        nfcStatusCode = -1;
                        onNfcFail();
                        try {
                            ndef.close();
                        } catch (IOException ex) {
                            Log.d(TAG, "Failed to close NDEF resources!");

                        }
                    }
                } else {
                    nfcStatusCode = -1;
                    onNfcFail();
                }
            }
        }
    }

    public int readStudentIdNumber(){
        // store the userID in SharedPref
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(NfcHandlerActivity.SHARED_PREF_ID, Context.MODE_PRIVATE);
        int userID = sharedPref.getInt(NfcHandlerActivity.SHARED_PREF_ID_DATA_KEY, -1);
        isUserSignedIn = userID != -1;
        return userID;

    }

    public boolean isUserSignedIn() {
        readStudentIdNumber(); // keep data fresh
        return isUserSignedIn;
    }

    public boolean isNfcSupported() {
        return isNfcSupported;
    }
}

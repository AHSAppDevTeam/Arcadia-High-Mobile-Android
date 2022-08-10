package com.nfc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.HashUtil;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.IOException;
import java.util.Arrays;

/**
 * An activity that contains nfc features built-in
 */
public abstract class NfcHandlerActivity extends AppCompatActivity implements NfcResultHandler {

    private static final String TAG = "NfcHandlerActivity";

    private PendingIntent pendingIntent;

    private IntentFilter[] intentFiltersArray = null;

    private NfcAdapter nfcAdapter;
    private boolean isNfcSupported = false;

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

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFiltersArray = new IntentFilter[]{ndef,};
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter != null && isNfcSupported)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(nfcAdapter != null && isNfcSupported)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (Arrays.asList(tag.getTechList()).contains(Ndef.class.getName())) { // ensure NDEF
                Ndef ndef = Ndef.get(tag);

                if (ndef != null) {
                    // tag is for sure ndef
                    try {
//                        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
//                        packer.packInt(39073);

                        int idNumber = 39073;

                        byte[] serializedData = new byte[3];

                        // create serializedData
                        serializedData[1] = (byte) (idNumber & 0xFF);
                        serializedData[2] = (byte) ((idNumber >> 8) & 0xFF);

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
                            onNfcSuccess();
                        } else {
                            Log.d(TAG, "Not writable!");
                            onNfcFail();
                        }
                        ndef.close();
                    } catch (Exception e) {
                        // tag failed to write
                        onNfcFail();
                    }
                } else {
                    onNfcFail();
                }
            }
        }
    }

    public boolean isNfcSupported() {
        return isNfcSupported;
    }
}

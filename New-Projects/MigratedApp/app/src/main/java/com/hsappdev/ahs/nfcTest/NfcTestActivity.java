package com.hsappdev.ahs.nfcTest;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.HashUtil;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NfcTestActivity extends AppCompatActivity {

    private static final String TAG = "NfcTestActivity";

    private IntentFilter[] intentFiltersArray = null;

    private NfcAdapter nfcAdapter;

    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_test);


        onNfcIntent(getIntent());



        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE);

        byte[] signed = HashUtil.getSha256Hash(new byte[] {73, 105, 73, 105}, new byte[0]);

        int[] unsigned = new int[signed.length];
        for (int i = 0; i < signed.length; i++) {
            unsigned[i] = signed[i] & 0xFF;
        }

        String log = Arrays.toString(unsigned);

        Log.d(TAG, "HASH: " + log);

        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        byte[] serializedData = packer.toByteArray();
        byte[] password = getResources().getString(R.string.super_secret_nfc_salt).getBytes(StandardCharsets.US_ASCII);

        byte[] hash = HashUtil.getSha256Hash(serializedData, password);


        NdefRecord ndefRecordHash = NdefRecord.createExternal("ahs", "nfc_h", hash);
        NdefRecord ndefRecordData = NdefRecord.createExternal("ahs", "nfc_d", serializedData);

        NdefMessage ndefMessage = new NdefMessage(ndefRecordHash, ndefRecordData);


        unsigned = new int[ndefMessage.toByteArray().length];
        for (int i = 0; i < ndefMessage.toByteArray().length; i++) {
            unsigned[i] = ndefMessage.toByteArray()[i] & 0xFF;
        }

        log = Arrays.toString(unsigned).replaceAll(",", "");


        Log.d(TAG, "HASH REAL: " + log);


        signed = "(this is the salt) mmm hash browns".getBytes(StandardCharsets.US_ASCII);

        unsigned = new int[signed.length];
        for (int i = 0; i < signed.length; i++) {
            unsigned[i] = signed[i] & 0xFF;
        }

        log = Arrays.toString(unsigned);

        Log.d(TAG, "SALT: " + log);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFiltersArray = new IntentFilter[]{ndef,};
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);

    }

    public void onResume() {
        super.onResume();
        if(nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onNfcIntent(intent);


    }

    private void onNfcIntent(Intent intent) {
        Log.d(TAG, "Tag detected: " + intent.getAction());

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            String techList = Arrays.toString(tag.getTechList());

            Log.d(TAG, "Tech List: " + techList);

            if (Arrays.asList(tag.getTechList()).contains(Ndef.class.getName())) {
                Ndef ndef = Ndef.get(tag);
                Log.d(TAG, "maxSize Tag: " + ndef.getMaxSize());


                if (ndef != null) {
                    // tag is for sure ndef
                    try {
                        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
                        packer.packInt(39073);

                        byte[] serializedData = packer.toByteArray();
                        byte[] password = getResources().getString(R.string.super_secret_nfc_salt).getBytes(StandardCharsets.US_ASCII);

                        byte[] hash = HashUtil.getSha256Hash(serializedData, password);


                        NdefRecord ndefRecordHash = NdefRecord.createExternal("a", "h", Arrays.copyOfRange(hash, 0, 16));
                        NdefRecord ndefRecordData = NdefRecord.createExternal("a", "d", serializedData);

                        NdefMessage ndefMessage = new NdefMessage(ndefRecordHash, ndefRecordData);


                        int[] unsigned = new int[16];
                        for (int i = 0; i < 16; i++) {
                            unsigned[i] = hash[i] & 0xFF;
                        }

                        String log = Arrays.toString(unsigned);

                        Log.d(TAG, "HASH REAL: " + log);

                        // TODO: 6/30/2022 remove this, just testing
                        if(Arrays.equals(HashUtil.getSha256Hash(serializedData, "(this is the salt) mmm hash browns".getBytes(StandardCharsets.US_ASCII)), hash)){
                            Log.d(TAG, "Data confirmed!");
                        }

                        Log.d(TAG, "Connecting...");
                        ndef.connect();
                        Log.d(TAG, "Connected");
                        if (ndef.isWritable()) {
                            Log.d(TAG, "maxSize Tag1: " + ndefMessage.getByteArrayLength());

                            ndef.writeNdefMessage(ndefMessage);
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(250);
                            }
                            Log.d(TAG, "Data sent!");
                            Toast.makeText(this, "Data sent!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Not writable!");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        // tag failed to write
                        try {
                            ndef.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }

                } else {
                    Toast.makeText(this, "Could not connect!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
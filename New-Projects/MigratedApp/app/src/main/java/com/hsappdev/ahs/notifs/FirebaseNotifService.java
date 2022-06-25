package com.hsappdev.ahs.notifs;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hsappdev.ahs.db.ArticleRepository;

public class FirebaseNotifService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseNotifService";

    private ArticleRepository articleRepository;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d(TAG, "Message Received from: " + message.getFrom());
    }
}

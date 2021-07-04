package com.hsappdev.ahs.firebaseMessaging;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

import java.util.List;

public class NotificationSetup {
    private static final String successMSG = "Successfully subscribed to: ";

    private static final String failureMSG = "Failed to subscribe to: ";

    public static void setUpNotificationChannel(Resources r, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = r.getString(R.string.notificationChannelID);
            String channelName = r.getString(R.string.notificationChannelName);
            NotificationManager notificationManager =
                    activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

    }

    public static void subscribe(Activity activity, List<String> channels) {
        for(String channel : channels) {
            FirebaseMessaging.getInstance().subscribeToTopic(channel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = successMSG + channel;
                            if (!task.isSuccessful()) {
                                msg = failureMSG + channel;
                            }
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}

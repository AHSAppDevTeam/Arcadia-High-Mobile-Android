package com.hsappdev.ahs.firebaseMessaging;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hsappdev.ahs.MainActivity;
import com.hsappdev.ahs.NotificationSettingsActivity;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.Helper;

import java.util.Arrays;
import java.util.List;

public class NotificationSetup {
    private static final String successMSG = "Successfully subscribed to: ";
    private static final String successUMSG = "Successfully unsubscribed from: ";

    private static final String failureMSG = "Failed to subscribe to: ";
    private static final String failureUMSG = "Failed to unsubscribe from: ";

    private static final String channelPrefix = "messageChannel_";
    public static final String[] channels = new String[]{"Debug", "Drafts"};


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

    public static void subscribe(Activity activity, String... channels) {
        for(String channel : channels) {
            // Make sure channel is selected
            if(getIfChannelIsEnabled(channel, activity)) {
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

    public static void setUp(Resources resources, MainActivity mainActivity) {
        setUpNotificationChannel(resources, mainActivity);
        NotificationSetup.subscribe(mainActivity, channels);
    }

    public static boolean getIfChannelIsEnabled(String channelName, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        boolean firstTime = sharedPref.getBoolean(channelPrefix.concat(channelName), true);
        return firstTime;
    }

    public static void setIfChannelIsEnabled(String channelName, boolean value, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(channelPrefix.concat(channelName), value);
        editor.apply();
    }

    public static void unsubscribeFromAllTopics() {
        for (String channel : channels) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(channel);
        }
    }

    public static void unsubscribe(Activity activity, String... channels) {
        for(String channel : channels) {
            // Make sure channel is not selected
            if(!getIfChannelIsEnabled(channel, activity)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(channel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = successUMSG + channel;
                                if (!task.isSuccessful()) {
                                    msg = failureUMSG + channel;
                                }
                                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}

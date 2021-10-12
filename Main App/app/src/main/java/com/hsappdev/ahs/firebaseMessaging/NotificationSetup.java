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

import com.google.firebase.messaging.FirebaseMessaging;
import com.hsappdev.ahs.MainActivity;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleCategoryIdLoader;
import com.hsappdev.ahs.cache.deprecated.OnCategoryListLoadedCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationSetup {

    private static final String TAG = "NotificationSetup";
    
    private static final String successMSG = "Successfully subscribed to: ";
    private static final String successUMSG = "Successfully unsubscribed from: ";

    private static final String failureMSG = "Failed to subscribe to: ";
    private static final String failureUMSG = "Failed to unsubscribe from: ";

    private static final String channelPrefix = "messageChannel_";
    public static List<String> channels;
    public static List<String> homeChannels = new ArrayList<>(),
                                communityChannels = new ArrayList<>(),
                                bulletinChannels = new ArrayList<>();


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

    public static void subscribe(Activity activity, String channel) {
        subscribe(activity, Collections.singletonList(channel), true);
    }
    public static void subscribe(Activity activity, List<String> channel) {
        subscribe(activity, channel, false);
    }
    public static void subscribe(Activity activity, List<String> channels, boolean showConfirmation) {
        for(String channel : channels) {
            // Make sure channel is selected
            if(getIfChannelIsEnabled(channel, activity)) {
                Log.d(TAG, "subscribe: " + channel);
                FirebaseMessaging.getInstance().subscribeToTopic(channel)
                        .addOnCompleteListener(task -> {
                            if(showConfirmation) {
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
        loadAllCategoryList(resources, categoryList -> {
            channels = categoryList;
            Log.d(TAG, "categoryListLoaded: " + channels.size());
            setUpNotificationChannel(resources, mainActivity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                channels.forEach(s -> Log.d(TAG, "accept: " + s));
            }
            NotificationSetup.subscribe(mainActivity, channels);
        });

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

    public static void unsubscribe(Activity activity, String channel) {
        unsubscribe(activity, Collections.singletonList(channel));
    }
    public static void unsubscribe(Activity activity, List<String> channels) {
        for(String channel : channels) {
            // Make sure channel is not selected
            if(!getIfChannelIsEnabled(channel, activity)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(channel)
                        .addOnCompleteListener(task -> {
                            String msg = successUMSG + channel;
                            if (!task.isSuccessful()) {
                                msg = failureUMSG + channel;
                            }
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }


    public static void loadAllCategoryList(Resources r, OnCategoryListLoadedCallback callback){
        List<String> allCategories = new ArrayList<>();
        ArticleCategoryIdLoader.loadSpecificCategoryList(r, categoryList -> {
            Log.d(TAG, "categoryListLoaded1: " + categoryList.size());
            allCategories.addAll(categoryList);
            homeChannels.clear();
            homeChannels.addAll(categoryList);
            ArticleCategoryIdLoader.loadSpecificCategoryList(r, categoryList1 -> {
                Log.d(TAG, "categoryListLoaded2: " + categoryList1.size());
                allCategories.addAll(categoryList1);
                bulletinChannels.clear();
                bulletinChannels.addAll(categoryList1);
                ArticleCategoryIdLoader.loadSpecificCategoryList(r, categoryList11 -> {
                    Log.d(TAG, "categoryListLoaded3: " + categoryList11.size());
                    allCategories.addAll(categoryList11);
                    communityChannels.clear();
                    communityChannels.addAll(categoryList11);
                    callback.categoryListLoaded(allCategories);
                }, r.getString(R.string.db_location_community));
            }, r.getString(R.string.db_location_bulletin));
        }, r.getString(R.string.db_location_ausdNews));



    }

}

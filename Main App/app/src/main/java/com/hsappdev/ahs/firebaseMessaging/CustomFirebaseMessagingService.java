package com.hsappdev.ahs.firebaseMessaging;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hsappdev.ahs.ArticleActivity;
import com.hsappdev.ahs.NotificationActivity;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.LoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "CustomFirebaseMessaging";

    private ArticleRepository articleRepository;
    public CustomFirebaseMessagingService() {

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(articleRepository == null)
            articleRepository = new ArticleRepository(getApplication());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> dataMap = remoteMessage.getData();
            String articleID = dataMap.get("articleID");
            ArticleLoaderBackend.getInstance((Application) getApplicationContext()).getCacheObject(articleID, getResources(), new LoadableCallback() {
                private boolean isFirstTime = false;
                @Override
                public <T> void onLoaded(T articleN) {
                    Article article = (Article) articleN;
                    article.setIsNotification(1); // 1 == true
                    //articleRepository.add(article);
                    RemoteMessage.Notification notification = remoteMessage.getNotification();
                    sendNotification(notification.getTitle(),
                            notification.getBody(),
                            getResources(),
                            article);
                    isFirstTime = true; // used to mimic activity destruction and remove this listener
                }

                @Override
                public boolean isActivityDestroyed() {
                    return isFirstTime;
                }
            });
        }

    }

    private void sendNotification(String title, String messageBody, Resources r, Article article) {
        Intent intent;
        if(article != null) {
            // if there is an article, show it
            intent = new Intent(this, ArticleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(ArticleActivity.data_KEY, article);
        } else {
            // else show the notif page
            intent = new Intent(this, NotificationActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String channelId = r.getString(R.string.notificationChannelID);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.profile_schedule_ic)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    r.getString(R.string.notificationChannelName),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(article.getArticleID().hashCode(), notificationBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

    }
}

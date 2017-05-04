package com.ebcnke.knulg.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import me.leolin.shortcutbadger.ShortcutBadger;


public class BadgeIntentService extends IntentService {

    private int notificationId = 0;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;

    private int lastCount = 1;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                mNotificationManager.cancel(notificationId);
                notificationId++;
                Notification.Builder builder = new Notification.Builder(getApplicationContext())
                        .setContentTitle("")
                        .setContentText("")
                        .setSmallIcon(com.ebcnke.knulg.R.mipmap.ic_launcher);
                Notification notification = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    notification = builder.build();
                } else {
                    notification = new Notification();
                }
                ShortcutBadger.applyNotification(getApplicationContext(), notification, notificationId % 2 == 0 ? 1 : 2);
                mNotificationManager.notify(notificationId, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

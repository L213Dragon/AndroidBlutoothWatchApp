package com.example.yura.martian_alerts.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Parcelable;
import android.support.v7.app.NotificationCompat;
import android.view.accessibility.AccessibilityEvent;

import com.example.yura.martian_alerts.R;

public class NotificationAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {
        if (e.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            Parcelable data = e.getParcelableData();
            if (data instanceof Notification) {

                if(((Notification)data).category != null && ((Notification)data).category.equals(Notification.CATEGORY_CALL)){
                    NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
                    ncomp.setContentTitle("Alert Notification");
                    ncomp.setContentText("NotificationPosted category call");
                    ncomp.setTicker("NotificationPosted");
                    ncomp.setSmallIcon(R.drawable.llist_enter);
                    ncomp.setAutoCancel(true);
                    nManager.notify((int) System.currentTimeMillis(), ncomp.build());
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

}

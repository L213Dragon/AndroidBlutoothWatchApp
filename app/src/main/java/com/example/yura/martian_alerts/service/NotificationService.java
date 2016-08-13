package com.example.yura.martian_alerts.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.yura.martian_alerts.R;
import com.example.yura.martian_alerts.entities.AlertMenuEntity;
import com.example.yura.martian_alerts.utils.DBHelper;

import java.util.Map;

public class NotificationService extends NotificationListenerService {
    private IBinder mWrapper = null;

    private final String TAG = "NLService";

    private final String CALL = Notification.CATEGORY_CALL;
    private final String EMAIL = Notification.CATEGORY_EMAIL;
    private final String SMS = Notification.CATEGORY_MESSAGE;
    private final String CALENDAR = Notification.CATEGORY_EVENT;
    private final String FACEBOOK = "com.facebook";
    private final String TWITTER = "com.twitter";
    private final String WHATSAPP = "com.whatsapp";
    private final String LINE = "com.line";
    private final String INSTAGRAM = "com.instagram";
    private final String PINTEREST = "com.pinterest";
    private final String GOOGLEPLUS = "com.google.android.apps.plus";


    private Context context;

    private DBHelper dbHelper;

    @Override
    public void onCreate() {
        dbHelper = new DBHelper(this);

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("Alert Notification");
        ncomp.setContentText("NotificationCreated");
        ncomp.setTicker("NotificationCreated");
        ncomp.setSmallIcon(R.drawable.llist_enter);
        ncomp.setAutoCancel(true);
        nManager.notify((int) System.currentTimeMillis(), ncomp.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        mWrapper = super.onBind(intent);

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("Alert Notification");
        ncomp.setContentText("NotificationBind");
        ncomp.setTicker("NotificationBind");
        ncomp.setSmallIcon(R.drawable.llist_enter);
        ncomp.setAutoCancel(true);
        nManager.notify((int) System.currentTimeMillis(), ncomp.build());

        return mWrapper;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("Alert Notification");
        ncomp.setContentText("NotificationStarted");
        ncomp.setTicker("NotificationStarted");
        ncomp.setSmallIcon(R.drawable.llist_enter);
        ncomp.setAutoCancel(true);
        nManager.notify((int) System.currentTimeMillis(), ncomp.build());

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("Alert Notification");
        ncomp.setContentText("NotificationDestroy");
        ncomp.setTicker("NotificationDestroy");
        ncomp.setSmallIcon(R.drawable.llist_enter);
        ncomp.setAutoCancel(true);
        nManager.notify((int) System.currentTimeMillis(), ncomp.build());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if(!sbn.getPackageName().equals("com.example.yura.martian_alerts")) {
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("Alert Notification");
            ncomp.setContentText("NotificationPosted category " + sbn.getNotification().category);
            ncomp.setTicker("NotificationPosted");
            ncomp.setSmallIcon(R.drawable.llist_enter);
            ncomp.setAutoCancel(true);
            nManager.notify((int) System.currentTimeMillis(), ncomp.build());
        }

        boolean ourEvent = false;
        AlertMenuEntity entity = null;
        String category = sbn.getNotification().category;
        if(sbn.getNotification().category != null){
            if(category.equals(CALL)){
                ourEvent = true;
                entity = dbHelper.getAlertSettingMenu("Call");
            }else if(category.equals(SMS)){
                ourEvent = true;
                entity = dbHelper.getAlertSettingMenu("SMS");
            }else if(category.equals(CALENDAR)){
                ourEvent = true;
                entity = dbHelper.getAlertSettingMenu("Calendar");
            }else if(category.equals(EMAIL)){
                ourEvent = true;
                entity = dbHelper.getAlertSettingMenu("E-mail");
            }
        }

        if(!ourEvent){
            String packageName = sbn.getPackageName();

            if(packageName.contains(FACEBOOK)){
                entity = dbHelper.getAlertSettingMenu("Facebook");
            }

            if(packageName.contains(TWITTER)){
                entity = dbHelper.getAlertSettingMenu("Twitter");
            }

            if(packageName.contains(WHATSAPP)){
                entity = dbHelper.getAlertSettingMenu("Whatsapp");
            }

            if(packageName.contains(LINE)){
                entity = dbHelper.getAlertSettingMenu("Line");
            }

            if(packageName.contains(INSTAGRAM)){
                entity = dbHelper.getAlertSettingMenu("Instagram");
            }

            if(packageName.contains(PINTEREST)){
                entity = dbHelper.getAlertSettingMenu("Pinterest");
            }

            if(packageName.contains(GOOGLEPLUS)){
                entity = dbHelper.getAlertSettingMenu("GooglePlus");
            }
        }
        Log.d(TAG,"Notification posted. CATEGORY :" + sbn.getNotification().category + " ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        if(entity != null){
            Log.d(TAG,"Send notifications");

            Map<Integer, Integer> map = entity.getVibrationType();
            String vibrationType = "(" + map.get(1) + "," + map.get(2) + "," + map.get(3) + "," + map.get(4) + ")";

            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("Alert Notification");
            ncomp.setContentText("Event : " + entity.getName() + " vibrationType" + vibrationType);
            ncomp.setTicker("Event : " + entity.getName() + " vibrationType" + vibrationType);
            ncomp.setSmallIcon(R.drawable.llist_enter);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }

    }

    /*@Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Intent i = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");

        sendBroadcast(i);
    }*/

    /*public static void registerForNotifications(StatusBarNotificationListenerInterface listener) {
        if (!toBeNotified.contains(listener)) {
            Log.v(TAG, listener.getClass().getCanonicalName() + " added to toBeNotified list");
            toBeNotified.add(listener);
        }
    }*/
}

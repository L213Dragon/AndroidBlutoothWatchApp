package com.example.yura.martian_alerts.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yura.martian_alerts.entities.AlertMenuEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    private final String ALERTS_DATABASE_TABLE = "alertsettings";

    private final static String LOG_TAG = "DBHelper";

    private List<String> enables = new ArrayList();
    private List<String> disabled = new ArrayList();

    public void initPresetting(){

        Cursor c = getWritableDatabase().query("alertsettings", null, null, null, null, null, null);

        if(!c.moveToFirst()) {

            if (enables.size() == 0) {
                enables.add("Call");
                enables.add("Missed Call");
                enables.add("SMS");
                enables.add("Calendar");
                enables.add("E-mail");
                enables.add("Voice mail");
            }

            if (disabled.size() == 0) {
                disabled.add("Facebook");
                disabled.add("Twitter");
                disabled.add("Whatsapp");
                disabled.add("Line");
                disabled.add("Instagram");
                disabled.add("Pinterest");
                disabled.add("GooglePlus");
            }

            ContentValues cv = new ContentValues();

            SQLiteDatabase db = getWritableDatabase();

            for (String s : enables) {
                cv.put("name", s);
                cv.put("enabled", true);
                cv.put("urgent", true);
                cv.put("vibration_1", 1);
                cv.put("vibration_2", 2);
                cv.put("vibration_3", 0);
                cv.put("vibration_4", 2);

                db.insert(ALERTS_DATABASE_TABLE, null, cv);
            }

            for (String s : disabled) {
                cv.put("name", s);
                cv.put("enabled", false);
                cv.put("urgent", false);
                cv.put("vibration_1", 1);
                cv.put("vibration_2", 2);
                cv.put("vibration_3", 0);
                cv.put("vibration_4", 2);

                db.insert(ALERTS_DATABASE_TABLE, null, cv);
            }
        }
    }

    public boolean update(AlertMenuEntity elementMenu){
        SQLiteDatabase db = getWritableDatabase();
        Map<Integer, Integer> vibrationType = elementMenu.getVibrationType();

        ContentValues cv = new ContentValues();
        cv.put("name", elementMenu.getName());
        cv.put("enabled", elementMenu.isEnable());
        cv.put("urgent", elementMenu.isUrgent());
        cv.put("vibration_1", vibrationType.get(1));
        cv.put("vibration_2", vibrationType.get(2));
        cv.put("vibration_3", vibrationType.get(3));
        cv.put("vibration_4", vibrationType.get(4));
        return db.update(ALERTS_DATABASE_TABLE, cv, "id"+ "=" + elementMenu.getId(), null) > 0;
    }

    public DBHelper(Context context) {
        super(context, "alertsSettingsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table alertsettings ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "enabled bool,"
                + "urgent bool,"
                + "vibration_1 number,"
                + "vibration_2 number,"
                + "vibration_3 number,"
                + "vibration_4 number"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public AlertMenuEntity getAlertSettingMenu(String menuName){
        Cursor c = getWritableDatabase().query("alertsettings", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                if (c.getInt(c.getColumnIndex("enabled")) == 1 && c.getString(c.getColumnIndex("name")).equals(menuName)) {
                    int iUrgent = c.getInt(c.getColumnIndex("urgent"));

                    AlertMenuEntity alertMenuEntity = new AlertMenuEntity(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")), true, (iUrgent == 1) ? true : false);
                    alertMenuEntity.addVibrationType(1, c.getInt(c.getColumnIndex("vibration_1")));
                    alertMenuEntity.addVibrationType(2, c.getInt(c.getColumnIndex("vibration_2")));
                    alertMenuEntity.addVibrationType(3, c.getInt(c.getColumnIndex("vibration_3")));
                    alertMenuEntity.addVibrationType(4, c.getInt(c.getColumnIndex("vibration_4")));

                    return alertMenuEntity;
                }
                ;
            } while (c.moveToNext());
        }
        return null;
    }
}

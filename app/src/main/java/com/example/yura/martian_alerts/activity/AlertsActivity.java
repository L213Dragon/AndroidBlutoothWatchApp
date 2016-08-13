package com.example.yura.martian_alerts.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.yura.martian_alerts.R;
import com.example.yura.martian_alerts.core.BaseActivity;
import com.example.yura.martian_alerts.entities.AlertMenuEntity;
import com.example.yura.martian_alerts.fragments.AlertsListFragment;
import com.example.yura.martian_alerts.fragments.AlertsSettingsFragment;
import com.example.yura.martian_alerts.service.NotificationService;
import com.example.yura.martian_alerts.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertsActivity extends BaseActivity implements AlertsSettingsFragment.onAlertsSettingsChangedListener, AlertsListFragment.OnAlertsListEventListener{

    private final String SIMPLE_MODE = "simple_mode";

    private AlertMenuEntity curSelectedAlertMenu;
    private ImageView ivBackImage;
    private TextView tvAlertMenu;

    private AlertsListFragment alertsListFragment;
    private AlertsSettingsFragment alertsSettingsFragment;

    private SharedPreferences sPref;

    private List<AlertMenuEntity> enables = new ArrayList();
    private List<AlertMenuEntity> disableds = new ArrayList();

    private DBHelper dbHelper;

    private Boolean isSimpleMode;

    private void getElementsMenu(){
        enables.clear();
        disableds.clear();

        Cursor c = dbHelper.getWritableDatabase().query("alertsettings", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                if(c.getInt(c.getColumnIndex("enabled")) == 1) {
                    int iUrgent = c.getInt(c.getColumnIndex("urgent"));

                    AlertMenuEntity alertMenuEntity = new AlertMenuEntity(c.getInt(c.getColumnIndex("id")) ,c.getString(c.getColumnIndex("name")) ,true, (iUrgent == 1)? true :false);
                    alertMenuEntity.addVibrationType(1, c.getInt(c.getColumnIndex("vibration_1")));
                    alertMenuEntity.addVibrationType(2, c.getInt(c.getColumnIndex("vibration_2")));
                    alertMenuEntity.addVibrationType(3, c.getInt(c.getColumnIndex("vibration_3")));
                    alertMenuEntity.addVibrationType(4, c.getInt(c.getColumnIndex("vibration_4")));
                    enables.add(alertMenuEntity);
                }else {
                    int iUrgent = c.getInt(c.getColumnIndex("urgent"));

                    AlertMenuEntity alertMenuEntity = new AlertMenuEntity(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")) ,false, (iUrgent == 1)? true :false);
                    alertMenuEntity.addVibrationType(1, c.getInt(c.getColumnIndex("vibration_1")));
                    alertMenuEntity.addVibrationType(2, c.getInt(c.getColumnIndex("vibration_2")));
                    alertMenuEntity.addVibrationType(3, c.getInt(c.getColumnIndex("vibration_3")));
                    alertMenuEntity.addVibrationType(4, c.getInt(c.getColumnIndex("vibration_4")));
                    disableds.add(alertMenuEntity);
                };
            } while (c.moveToNext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        findViews();

        sPref = getPreferences(MODE_PRIVATE);
        isSimpleMode = sPref.getBoolean(SIMPLE_MODE, false);

        dbHelper = new DBHelper(this);
        dbHelper.initPresetting();

        getElementsMenu();

        alertsListFragment = new AlertsListFragment();
        alertsSettingsFragment = new AlertsSettingsFragment();
        initFragment(alertsListFragment);

        addListener();

        //stopService(new Intent(this, NotificationService.class));

        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));

        //Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        //startActivity(intent);

        startService(new Intent(this, NotificationService.class));
        /*bindService(new Intent(this, NotificationService.class), new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);*/

        //startService(new Intent(this, NotificationAccessibilityService.class));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //startService(new Intent(this, NotificationService.class));
    }

    private void findViews(){
        tvAlertMenu = (TextView) findViewById(R.id.alertMenuText);
        tvAlertMenu.setText("Alert Filter");

        ivBackImage = (ImageView)findViewById(R.id.alertBackImageView);
    }

    private void addListener(){
        alertsSettingsFragment.setOnAlertsSettingsChangedListener(this);

        alertsListFragment.setOnAlertsListEventListener(this);

        ivBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAlertMenu.setText("Alert Filter");
                tvAlertMenu.setGravity(Gravity.CENTER);
                ivBackImage.setVisibility(View.GONE);

                changeFragment(alertsListFragment);

                alertsListFragment.setSimpleMode(isSimpleMode);

                getElementsMenu();
            }
        });
    }

    @Override
    public void initFragment(Fragment f) {
        alertsListFragment.setEnables(enables);
        alertsListFragment.setDisabled(disableds);
        alertsListFragment.setSimpleMode(isSimpleMode);

        super.initFragment(f);
    }

    @Override
    public void onEnabledChanged(boolean isChecked) {
        dbHelper.update(curSelectedAlertMenu);
    }

    @Override
    public void onUrgentChanged(boolean isChecked) {
        dbHelper.update(curSelectedAlertMenu);
    }

    @Override
    public void onSeekBarProgressChanged(SeekBar seekBar, int progress) {
        dbHelper.update(curSelectedAlertMenu);
    }

    @Override
    public void onClickEnter(Map<String, Object> tag) {
        if(tag.get("type").equals("enable")) {
            curSelectedAlertMenu = enables.get((Integer)tag.get("position"));
        }else {
            curSelectedAlertMenu = disableds.get((Integer) tag.get("position"));
        }
        alertsSettingsFragment.setElementMenu(curSelectedAlertMenu);
        tvAlertMenu.setText(curSelectedAlertMenu.getName());
        tvAlertMenu.setGravity(Gravity.CENTER_VERTICAL);
        ivBackImage.setVisibility(View.VISIBLE);

        changeFragment(alertsSettingsFragment);

        alertsSettingsFragment.setSimpleMode(isSimpleMode);
    }

    private void setSimpleVibroMode(){
        for(AlertMenuEntity entity : enables){
            Map<Integer, Integer> map = entity.getVibrationType();
            if(entity.isUrgent()){
                map.put(1,1);
                map.put(2,1);
                map.put(3,1);
                map.put(4,1);
            }else{
                map.put(1,1);
                map.put(2,0);
                map.put(3,1);
                map.put(4,0);
            }

            dbHelper.update(entity);
        }

        for(AlertMenuEntity entity : disableds){
            Map<Integer, Integer> map = entity.getVibrationType();
            if(entity.isUrgent()){
                map.put(1,1);
                map.put(2,1);
                map.put(3,1);
                map.put(4,1);
            }else{
                map.put(1,1);
                map.put(2,0);
                map.put(3,1);
                map.put(4,0);
            }

            dbHelper.update(entity);
        }

    }

    @Override
    public void onSimpleModeChecked(boolean isChecked) {

        if(isChecked && !isSimpleMode) {

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            LinearLayout view = (LinearLayout) getLayoutInflater()
                    .inflate(R.layout.alerts_popup, null);
            adb.setView(view);
            final AlertDialog alertDialog = adb.show();
            TextView alertDialogSwitch = (TextView) view.findViewById(R.id.alertDialogSwitch);
            TextView alertDialogCancel = (TextView) view.findViewById(R.id.alertDialogCancel);

            alertDialogSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSimpleMode = true;
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putBoolean(SIMPLE_MODE, isSimpleMode);
                    ed.commit();

                    alertDialog.dismiss();

                    setSimpleVibroMode();

                    alertsListFragment.scSwitchSimpleMode.setChecked(true);
                }
            });

            alertDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            alertsListFragment.scSwitchSimpleMode.setChecked(false);
        }else {

            isSimpleMode = isChecked;
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean(SIMPLE_MODE, isSimpleMode);
            ed.commit();
        }

        alertsListFragment.setSimpleMode(isSimpleMode);
        alertsListFragment.updateMenu();
    }
}

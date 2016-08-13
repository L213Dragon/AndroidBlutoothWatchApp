package com.example.yura.martian_alerts.fragments;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.yura.martian_alerts.R;
import com.example.yura.martian_alerts.core.BaseFragment;
import com.example.yura.martian_alerts.entities.AlertMenuEntity;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;

import java.util.Map;

public class AlertsSettingsFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener{

    public interface onAlertsSettingsChangedListener {

        public void onEnabledChanged(boolean isChecked);

        public void onUrgentChanged(boolean isChecked);

        public void onSeekBarProgressChanged(SeekBar seekBar, int progress);
    }

    private SwitchCompat scSwitchUrgent;
    private SwitchCompat scSwitchEnabled;
    private LinearLayout llMainContain;

    private onAlertsSettingsChangedListener alertsSettingsChangedListener;

    private AlertMenuEntity elementMenu;

    private VerticalSeekBar vsb_1;
    private VerticalSeekBar vsb_2;
    private VerticalSeekBar vsb_3;
    private VerticalSeekBar vsb_4;

    private boolean onResume = false;
    private boolean simpleMode;

    public void setElementMenu(AlertMenuEntity elementMenu){
        this.elementMenu = elementMenu;
    }

    public void setOnAlertsSettingsChangedListener(onAlertsSettingsChangedListener alertsSettingsChangedListener){
        this.alertsSettingsChangedListener = alertsSettingsChangedListener;
    }

    public void setSimpleMode(boolean simpleMode){
        this.simpleMode = simpleMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        onResume = false;

        final View view = inflater.inflate(R.layout.alerts_settings_fragment, container, false);

        final TextView tvElement = (TextView)view.findViewById(R.id.alertSettingsTextElement);
        tvElement.setText(elementMenu.getName());

        llMainContain = (LinearLayout)view.findViewById(R.id.alertSettingsMainContain);

        scSwitchEnabled = (SwitchCompat)view.findViewById(R.id.alertSettingEnableSwitch);
        scSwitchEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onResume) {
                    if (isChecked)
                        llMainContain.setVisibility(View.VISIBLE);
                    else
                        llMainContain.setVisibility(View.INVISIBLE);

                    elementMenu.setEnable(isChecked);
                    alertsSettingsChangedListener.onEnabledChanged(isChecked);
                }
            }
        });

        scSwitchUrgent = (SwitchCompat)view.findViewById(R.id.alertSettingsUrgentSwitch);
        scSwitchUrgent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onResume) {
                    elementMenu.setUrgent(isChecked);
                    alertsSettingsChangedListener.onUrgentChanged(isChecked);

                    if(simpleMode) setVibrationPattern();
                }
            }
        });



        vsb_1 = (VerticalSeekBar)view.findViewById(R.id.alertSettingVerticalSeekbar_1);
        vsb_2 = (VerticalSeekBar)view.findViewById(R.id.alertSettingVerticalSeekbar_2);
        vsb_3 = (VerticalSeekBar)view.findViewById(R.id.alertSettingVerticalSeekbar_3);
        vsb_4 = (VerticalSeekBar)view.findViewById(R.id.alertSettingVerticalSeekbar_4);

        vsb_1.setTag(1);
        vsb_2.setTag(2);
        vsb_3.setTag(3);
        vsb_4.setTag(4);

        vsb_1.setOnSeekBarChangeListener(this);
        vsb_2.setOnSeekBarChangeListener(this);
        vsb_3.setOnSeekBarChangeListener(this);
        vsb_4.setOnSeekBarChangeListener(this);

        if(simpleMode){
            vsb_1.setEnabled(false);
            vsb_2.setEnabled(false);
            vsb_3.setEnabled(false);
            vsb_4.setEnabled(false);
        }

        LinearLayout llUrgentLayout = (LinearLayout)view.findViewById(R.id.alertSettingsUrgentLayout);
        if(simpleMode)
            llUrgentLayout.setVisibility(View.VISIBLE);
        else
            llUrgentLayout.setVisibility(View.GONE);

        final TextView tvPlayPattern = (TextView)view.findViewById(R.id.alertSettingPlayPatternTextView);
        tvPlayPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void setVibrationPattern(){
        if(elementMenu.isUrgent()){
            vsb_1.setProgress(1);
            vsb_2.setProgress(1);
            vsb_3.setProgress(1);
            vsb_4.setProgress(1);
        }else{
            vsb_1.setProgress(1);
            vsb_2.setProgress(0);
            vsb_3.setProgress(1);
            vsb_4.setProgress(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scSwitchUrgent.setChecked(elementMenu.isUrgent());
        scSwitchEnabled.setChecked(elementMenu.isEnable());

        if(elementMenu.isEnable())
            llMainContain.setVisibility(View.VISIBLE);
        else
            llMainContain.setVisibility(View.INVISIBLE);

        if(!simpleMode) {
            Map<Integer, Integer> vibrationType = elementMenu.getVibrationType();
            vsb_1.setProgress(vibrationType.get(1));
            vsb_2.setProgress(vibrationType.get(2));
            vsb_3.setProgress(vibrationType.get(3));
            vsb_4.setProgress(vibrationType.get(4));
        }else{
            setVibrationPattern();
        }

        onResume = true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(onResume) {
            Map<Integer, Integer> vibrationType = elementMenu.getVibrationType();
            vibrationType.put((Integer) seekBar.getTag(), progress);

            alertsSettingsChangedListener.onSeekBarProgressChanged(seekBar, progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

package com.example.yura.martian_alerts.fragments;


import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yura.martian_alerts.R;
import com.example.yura.martian_alerts.core.BaseFragment;
import com.example.yura.martian_alerts.entities.AlertMenuEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertsListFragment extends BaseFragment implements View.OnClickListener{

    public interface OnAlertsListEventListener{
        public void onClickEnter(Map<String, Object> tag);

        public void onSimpleModeChecked(boolean isChecked);
    }

    public SwitchCompat scSwitchSimpleMode;
    private LinearLayout llEnabled;
    private LinearLayout llDisabled;

    private OnAlertsListEventListener alertsListEventListener;

    private List<AlertMenuEntity> enables;
    private List<AlertMenuEntity> disabled;

    private boolean onResume;
    private boolean simpleMode;

    public void setEnables(List<AlertMenuEntity> enables){
        this.enables = enables;
    }

    public void setDisabled(List<AlertMenuEntity> disabled){
        this.disabled = disabled;
    }

    public void setOnAlertsListEventListener(OnAlertsListEventListener alertsListEventListener) {
        this.alertsListEventListener = alertsListEventListener;
    }

    public void setSimpleMode(Boolean simpleMode){
        this.simpleMode = simpleMode;
    }

    public void updateMenu(){
        int i = 0;

        llEnabled.removeAllViewsInLayout();;
        llDisabled.removeAllViewsInLayout();

        for(AlertMenuEntity entity : enables){
            View v = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.alerts_row_enable, null, false);
            llEnabled.addView(v);

            TextView tv = (TextView)v.findViewById(R.id.alertRowEnabledTextView);
            tv.setText(entity.getName());

            LinearLayout llVibroPrew = (LinearLayout) v.findViewById(R.id.alertRowEnabledVibroPrew);

            if(!simpleMode) {

                llVibroPrew.setVisibility(View.VISIBLE);

                LinearLayout llLong1 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew1Long);
                LinearLayout llShort1 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew1Short);

                LinearLayout llLong2 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew2Long);
                LinearLayout llShort2 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew2Short);

                LinearLayout llLong3 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew3Long);
                LinearLayout llShort3 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew3Short);

                LinearLayout llLong4 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew4Long);
                LinearLayout llShort4 = (LinearLayout) v.findViewById(R.id.alertRowEnableVibroPrew4Short);

                Map<Integer, Integer> map = entity.getVibrationType();

                switch (map.get(1)) {
                    case 0:
                        llLong1.setVisibility(View.VISIBLE);
                        llShort1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        llLong1.setVisibility(View.VISIBLE);
                        llShort1.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        llLong1.setVisibility(View.INVISIBLE);
                        llShort1.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }

                switch (map.get(2)) {
                    case 0:
                        llLong2.setVisibility(View.VISIBLE);
                        llShort2.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        llLong2.setVisibility(View.VISIBLE);
                        llShort2.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        llLong2.setVisibility(View.INVISIBLE);
                        llShort2.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }

                switch (map.get(3)) {
                    case 0:
                        llLong3.setVisibility(View.VISIBLE);
                        llShort3.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        llLong3.setVisibility(View.VISIBLE);
                        llShort3.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        llLong3.setVisibility(View.INVISIBLE);
                        llShort3.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }

                switch (map.get(4)) {
                    case 0:
                        llLong4.setVisibility(View.VISIBLE);
                        llShort4.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        llLong4.setVisibility(View.VISIBLE);
                        llShort4.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        llLong4.setVisibility(View.INVISIBLE);
                        llShort4.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
            }else{

                llVibroPrew.setVisibility(View.INVISIBLE);

            }

            ImageView mEnterImage = (ImageView) v.findViewById(R.id.alertRowEnterimageView);
            Map<String, Object> tag = new HashMap<>();
            tag.put("type", "enable");
            tag.put("position", i);
            mEnterImage.setTag(tag);
            mEnterImage.setOnClickListener(this);

            i++;

        }

        i = 0;
        for(AlertMenuEntity entity : disabled){
            View v = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.alerts_row_enable, null, false);
            llDisabled.addView(v);

            TextView tv = (TextView)v.findViewById(R.id.alertRowEnabledTextView);
            tv.setText(entity.getName());

            LinearLayout llVibroPrew = (LinearLayout) v.findViewById(R.id.alertRowEnabledVibroPrew);
            llVibroPrew.setVisibility(View.INVISIBLE);

            ImageView mEnterImage = (ImageView) v.findViewById(R.id.alertRowEnterimageView);
            Map<String, Object> tag = new HashMap<>();
            tag.put("type", "disable");
            tag.put("position", i);
            mEnterImage.setTag(tag);
            mEnterImage.setOnClickListener(this);

            i++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        onResume = false;

        View view = inflater.inflate(R.layout.alerts_list_fragment, container, false);

        scSwitchSimpleMode = (SwitchCompat)view.findViewById(R.id.alertSimpleModeSwitch);

        scSwitchSimpleMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onResume)
                    alertsListEventListener.onSimpleModeChecked(isChecked);
            }
        });

        llEnabled = (LinearLayout)view.findViewById(R.id.alertLayoutEnable);
        llDisabled = (LinearLayout)view.findViewById(R.id.alertLayoutDisable);

        updateMenu();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        scSwitchSimpleMode.setChecked(simpleMode);
        onResume = true;
    }

    @Override
    public void onClick(View v) {
        alertsListEventListener.onClickEnter((Map) v.getTag());
    }
}

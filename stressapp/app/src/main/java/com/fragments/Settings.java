package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.semis.stressapp.DataGatherer;
import com.example.semis.stressapp.MainActivity;
import com.example.semis.stressapp.R;
import com.example.semis.stressapp.TimeManager;

public class Settings extends Fragment {
    public Switch stat, battery;
    Intent it;
    public Switch getStat() {
        return stat;
    }

    public Switch getBattery() {
        return battery;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        it = new Intent(getContext(), DataGatherer.class);
        View current = inflater.inflate(R.layout.settings, container, false);
        stat = current.findViewById(R.id.onoff);
        battery = current.findViewById(R.id.low_batterymode);
        stat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TimeManager.getInstance().updateTime();

                    getActivity().startService(it);
                } else {
                    getActivity().stopService(it);
                    MainActivity.running = false;
                }
            }
        });
        battery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TimeManager.getInstance().setLowBattery(true);
                } else {
                    TimeManager.getInstance().setLowBattery(false);
                }
            }
        });
        return current;
    }
}

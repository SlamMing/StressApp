package com.example.semis.stressapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class DataGatherer extends Service implements SensorEventListener{
    private SensorManager sm;
    private Sensor acc;
    private IOmanager iom;
    private TimeManager tm;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //iomanager helps with I/O functions
        iom = IOmanager.getInstance();
        tm = TimeManager.getInstance();


        //debug
        Toast.makeText(this, "started", Toast.LENGTH_SHORT).show();


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(DataGatherer.this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();


        //debug
        Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!MainActivity.running) return;

        if(tm.isTimeToCollect())
            iom.addData(event.values[0], event.values[1], event.values[2]);

        if(tm.isTimeToWrite()) {
            iom.writeData();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}

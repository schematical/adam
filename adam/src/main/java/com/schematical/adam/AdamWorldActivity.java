package com.schematical.adam;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.schematical.adam.comm.AdamCommDriver;
//import com.schematical.adam.drawable.AdamWorldView;
import com.schematical.adam.signal.AdamSignalDriver;
import com.schematical.adam.sensors.AdamSensorDriver;
import com.google.android.gms.*;
import java.util.Timer;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamWorldActivity extends Activity {
    private static AdamWorldActivity instance;
    private AdamSignalDriver adamSignalDriver;
    private AdamSensorDriver sensorDriver;
    private AdamCommDriver adamCommDriver;
    //private AdamWorldView mView;
    private int checkinDuration_seconds = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        int test = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        String testStr = GooglePlayServicesUtil.getErrorString(test);
        sensorDriver = new AdamSensorDriver(this);
        sensorDriver.onResume();

        //mView = new AdamWorldView(this);
        //setContentView(mView);
        adamSignalDriver = new AdamSignalDriver();
        adamCommDriver = new AdamCommDriver();


        setContentView(R.layout.adam_map);
        StartCheckin();

    }

    private void StartCheckin() {
        Timer timer = new Timer();
        timer.schedule(
            new AdamSystemUpdater(),
            checkinDuration_seconds*1000
        );
    }

    public static AdamWorldActivity getInstance(){
        return instance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorDriver.onResume();
        adamSignalDriver.Connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorDriver.onPause();
        adamSignalDriver.Disconnect();
    }

}

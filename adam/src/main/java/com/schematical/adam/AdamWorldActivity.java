package com.schematical.adam;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.schematical.adam.async.AdamSaveDriver;
import com.schematical.adam.drawable.AdamWorldView;
import com.schematical.adam.drawable.opengl.AdamOpenGLView;
import com.schematical.adam.sensors.AdamSensorDriver;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamWorldActivity extends Activity {
    private static AdamWorldActivity instance;
    private AdamSensorDriver sensorDriver;
    private AdamSaveDriver saveDriver;
    private AdamWorldView mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        sensorDriver = new AdamSensorDriver(this);
        sensorDriver.onResume();

        mView = new AdamWorldView(this);
        setContentView(mView);
    }
    public static AdamWorldActivity getInstance(){
        return instance;
    }

}

package com.schematical.adam.drawable.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.schematical.adam.comm.async.AdamSaveDriver;
import com.schematical.adam.sensors.AdamSensorDriver;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamGLWorldActivity extends Activity {
    private GLSurfaceView mGLView;
    private AdamSensorDriver sensorDriver;
    private AdamSaveDriver saveDriver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorDriver = new AdamSensorDriver(this);
        sensorDriver.onResume();
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new AdamOpenGLView(this);
        setContentView(mGLView);
    }

}

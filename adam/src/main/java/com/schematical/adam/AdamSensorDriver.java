package com.schematical.adam;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamSensorDriver implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    private Sensor mAccelerometer;

    private float[] gravity = new float[3];
    private float[] geomag = new float[3];
    private int mCount = 0;

    public static final float[] rotationMatrix = new float[16];
    public static final float[] inclinationMatrix = new float[16];
    protected Context am;



    protected static Double currYaw;
    protected static Double currPitch;
    protected static Double currRoll;
    protected static Double currIncline;


    public AdamSensorDriver(Context nAm){
        am = nAm;
        mSensorManager = (SensorManager) am.getSystemService(Context.SENSOR_SERVICE);
        //mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onSensorChanged(SensorEvent evt) {
        int type=evt.sensor.getType();

        //Smoothing the sensor data a bit
        if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            geomag[0]=(geomag[0]*1+evt.values[0])*0.5f;
            geomag[1]=(geomag[1]*1+evt.values[1])*0.5f;
            geomag[2]=(geomag[2]*1+evt.values[2])*0.5f;
        } else if (type == Sensor.TYPE_ACCELEROMETER) {
            gravity[0]=(gravity[0]*2+evt.values[0])*0.33334f;
            gravity[1]=(gravity[1]*2+evt.values[1])*0.33334f;
            gravity[2]=(gravity[2]*2+evt.values[2])*0.33334f;
        }

        if ((type==Sensor.TYPE_MAGNETIC_FIELD) || (type==Sensor.TYPE_ACCELEROMETER)) {
            float[] mRotationMatrix = new float[16];
            SensorManager.getRotationMatrix(mRotationMatrix, inclinationMatrix, gravity, geomag);
            SensorManager.remapCoordinateSystem(
                mRotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                rotationMatrix
            );
            float[] mOrientation = new float[16];
            SensorManager.getOrientation(rotationMatrix, mOrientation);
            float incl = SensorManager.getInclination(inclinationMatrix);



            currYaw =((Float) mOrientation[0]).doubleValue();
            currPitch = ((Float)mOrientation[1]).doubleValue();
            currRoll = ((Float)mOrientation[2]).doubleValue();
            currIncline = ((Float)incl).doubleValue();

        }


    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void onResume(){
        mSensorManager.registerListener(
                this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME );
        mSensorManager.registerListener(
                this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME );
    }
    public void onPause(){
        mSensorManager.unregisterListener(this);
    }

    public static Double getCurrYaw() {
        return currYaw;
    }

    public static Double getCurrPitch() {
        return currPitch;
    }

    public static Double getCurrRoll() {
        return currRoll;
    }

    public static Double getCurrIncline() {
        return currIncline;
    }





}

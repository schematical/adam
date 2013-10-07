package com.schematical.adam;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamSensorDriver implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    private Sensor mAccelerometer;

    private float[] gravity = new float[3];
    private float[] geomag = new float[3];
    private float[] rotationMatrix = new float[16];

    protected AdamActivityMain am;
    public AdamSensorDriver(AdamActivityMain nAm){
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
            rotationMatrix = new float[16];
            SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomag);
            SensorManager.remapCoordinateSystem(
                    rotationMatrix,
                    SensorManager.AXIS_Y,
                    SensorManager.AXIS_MINUS_X,
                    rotationMatrix );
        }

        //Hail Mary
        am.GetView().UpdateOrientation(
                Math.cos(rotationMatrix[5]),
                Math.cos(rotationMatrix[0]),
                zAngle
        );
    }
    public axisAngle toAxisAngle(matrix m) {
        double angle,x,y,z; // variables for result
        double epsilon = 0.01; // margin to allow for rounding errors
        double epsilon2 = 0.1; // margin to distinguish between 0 and 180 degrees
        // optional check that input is pure rotation, 'isRotationMatrix' is defined at:
        // http://www.euclideanspace.com/maths/algebra/matrix/orthogonal/rotation/
        assert isRotationMatrix(m) : "not valid rotation matrix" ;// for debugging
        if ((Math.abs(m[0][1]-m[1][0])< epsilon)
                && (Math.abs(m[0][2]-m[2][0])< epsilon)
                && (Math.abs(m[1][2]-m[2][1])< epsilon)) {
            // singularity found
            // first check for identity matrix which must have +1 for all terms
            //  in leading diagonaland zero in other terms
            if ((Math.abs(m[0][1]+m[1][0]) < epsilon2)
                    && (Math.abs(m[0][2]+m[2][0]) < epsilon2)
                    && (Math.abs(m[1][2]+m[2][1]) < epsilon2)
                    && (Math.abs(m[0][0]+m[1][1]+m[2][2]-3) < epsilon2)) {
                // this singularity is identity matrix so angle = 0
                return new axisAngle(0,1,0,0); // zero angle, arbitrary axis
            }
            // otherwise this singularity is angle = 180
            angle = Math.PI;
            double xx = (m[0][0]+1)/2;
            double yy = (m[1][1]+1)/2;
            double zz = (m[2][2]+1)/2;
            double xy = (m[0][1]+m[1][0])/4;
            double xz = (m[0][2]+m[2][0])/4;
            double yz = (m[1][2]+m[2][1])/4;
            if ((xx > yy) && (xx > zz)) { // m[0][0] is the largest diagonal term
                if (xx< epsilon) {
                    x = 0;
                    y = 0.7071;
                    z = 0.7071;
                } else {
                    x = Math.sqrt(xx);
                    y = xy/x;
                    z = xz/x;
                }
            } else if (yy > zz) { // m[1][1] is the largest diagonal term
                if (yy< epsilon) {
                    x = 0.7071;
                    y = 0;
                    z = 0.7071;
                } else {
                    y = Math.sqrt(yy);
                    x = xy/y;
                    z = yz/y;
                }
            } else { // m[2][2] is the largest diagonal term so base result on this
                if (zz< epsilon) {
                    x = 0.7071;
                    y = 0.7071;
                    z = 0;
                } else {
                    z = Math.sqrt(zz);
                    x = xz/z;
                    y = yz/z;
                }
            }
            return new axisAngle(angle,x,y,z); // return 180 deg rotation
        }
        // as we have reached here there are no singularities so we can handle normally
        double s = Math.sqrt((m[2][1] - m[1][2])*(m[2][1] - m[1][2])
                +(m[0][2] - m[2][0])*(m[0][2] - m[2][0])
                +(m[1][0] - m[0][1])*(m[1][0] - m[0][1])); // used to normalise
        if (Math.abs(s) < 0.001) s=1;
        // prevent divide by zero, should not happen if matrix is orthogonal and should be
        // caught by singularity test above, but I've left it in just in case
        angle = Math.acos(( m[0][0] + m[1][1] + m[2][2] - 1)/2);
        x = (m[2][1] - m[1][2])/s;
        y = (m[0][2] - m[2][0])/s;
        z = (m[1][0] - m[0][1])/s;
        return new axisAngle(angle,x,y,z);
    }

    /* public void onSensorChanged(SensorEvent event) {

        double xAngle = (event.values[0]/180) * Math.PI;// * Math.PI / 2;
        double yAngle = (event.values[1]/180)  * Math.PI;// * Math.PI / 2;
        double zAngle = (event.values[2]/180) * Math.PI;// * Math.PI / 2;

        am.GetView().UpdateOrientation(xAngle, yAngle, zAngle);
    }*/
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






}

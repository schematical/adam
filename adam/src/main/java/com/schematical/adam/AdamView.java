package com.schematical.adam;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;

import android.view.*;
import android.view.View;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user1a on 9/30/13.
 */
public class AdamView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Canvas mCanvas;
    private Paint paint;
    private double xAngle;
    private double yAngle;
    private double zAngle;
    private AdamRadar mRadar;
    public Location mLoctaion;



    public AdamView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mRadar = new AdamRadar(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(40);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setWillNotDraw(false);

    }

    public void UpdateOrientation(double nXAngle,double nYAngle,double nZAngle){
        xAngle = nXAngle;
        yAngle = nYAngle;
        zAngle = nZAngle;
    }
    @Override
    public void onDraw(Canvas canvas){
        mCanvas = canvas;
        super.onDraw(canvas);

        //Draw radar
        mRadar.Draw(canvas, xAngle, yAngle, zAngle);


        String msg = "XYZ: " + Double.toString(xAngle) + "- Y:" + Double.toString(yAngle) + " - Z:" + Double.toString(zAngle);
        canvas.drawText(
                msg,
                100,
                100,
                paint
        );



        double mLat = -89.38712835104843;//mLoctaion.getLongitude()
        double mLng = 43.074808234970945;//mLoctaion.getLatitude()
        if(mLoctaion != null){
            mLat = mLoctaion.getLongitude();
            mLng = mLoctaion.getLatitude();
        }
        Hashtable<String, AdamObject> mObjects = ((AdamActivityMain) getContext()).GetAdamObjects();
        Enumeration names = mObjects.keys();
        while(names.hasMoreElements()) {
            String str = (String) names.nextElement();

            AdamObject mObject = (AdamObject) mObjects.get(str);

            //Find the diff between the Angle were facing and the angle of the object

            float x = 0;
            float y = 0;

            double lngDiff = mLng - mObject.GetLng();
            double latDiff = (mLat - mObject.GetLat());
            double distance = Math.sqrt(Math.pow(lngDiff, 2) + Math.pow(latDiff,2));
            double mObjectRelitiveAngle = Math.atan(lngDiff / latDiff);
            double mObjectAngleDiff = mObjectRelitiveAngle - xAngle;



            msg = "Diff Angle: " + Double.toString(mObjectAngleDiff) + "   - Relitive Angle: " + Double.toString(mObjectRelitiveAngle);
            canvas.drawText(
                    msg,
                    100,
                    200,
                    paint
            );

            double bigX = Math.cos(xAngle) * canvas.getWidth();
            double bigY = Math.sin(xAngle) * canvas.getWidth();//Only used to determin if we render or not
            double screenX = bigX + canvas.getWidth()/2;

            //Figure out how wide the angle of view seen by the camera is
            double viewWidth = Math.PI/2;
            //First determin if it is in the view range


           if(bigY > 0 ){
                mObject.SetGoalXY(
                    (int) Math.round(screenX),
                    Math.round(canvas.getHeight()/2)
                );
                mObject.Draw(canvas);
            }

        }
        postInvalidate();



    }
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("Adam", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("Adam", "Error starting camera preview: " + e.getMessage());
        }
    }

    public void UpdateLocation(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d("Adam",msg);
        this.mLoctaion = location;
    }

}
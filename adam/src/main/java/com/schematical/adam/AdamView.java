package com.schematical.adam;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.location.Location;
import android.util.Log;

import android.view.*;

import com.schematical.adam.drawable.AdamDrawable;
import com.schematical.adam.drawable.AdamHud;
import com.schematical.adam.drawable.AdamObjectHud;
import com.schematical.adam.drawable.AdamRadar;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user1a on 9/30/13.
 */
public class AdamView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private SurfaceHolder mHolder;
    private AdamHud ah;
    private Camera mCamera;
    private Canvas mCanvas;

    public double xAngle;
    public double yAngle;
    public double zAngle;
    private AdamRadar mRadar;


    public String focusObjectId = null;
    private Hashtable<String, AdamDrawable> controls = new Hashtable<String, AdamDrawable>();


    public AdamView(Context context, Camera camera) {
        super(context);
        this.setOnTouchListener(this);
        mCamera = camera;
        mRadar = new AdamRadar(this);
        mRadar.setBottom(20);
        mRadar.setRight(20);
        ah = new AdamHud(this);

        focusObjectId = "100 State";

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setWillNotDraw(false);

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Enumeration<String> keys = controls.keys();

        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamDrawable ad = (AdamDrawable) controls.get(key);
            if(
                (ad.getX() < x) &&
                (ad.getX() + ad.getWidth() > x) &&
                (ad.getY() < y) &&
                (ad.getY() + ad.getHeight() > y)
            ){
                ad.onTouch(v, event);
            }

        }
        return false;
    }

    public void UpdateOrientation(double nXAngle,double nYAngle,double nZAngle){
        xAngle = nXAngle;
        yAngle = nYAngle;
        zAngle = nZAngle;
    }

    public Canvas GetCanvas() {
        return mCanvas;
    }

    @Override
    public void onDraw(Canvas canvas){
        mCanvas = canvas;
        super.onDraw(canvas);

        //Draw radar
        mRadar.Draw(canvas, xAngle, yAngle, zAngle);




        AdamActivityMain am = ((AdamActivityMain) getContext());

        Location mLocation = am.GetLocation();
        if(mLocation != null){


            Hashtable<String, AdamObject> mObjects = am.GetAdamObjects();
            Enumeration names = mObjects.keys();
            String objId = null;
            while(names.hasMoreElements()) {
                objId = (String) names.nextElement();

                AdamObject mObject = (AdamObject) mObjects.get(objId);

                //Find the diff between the Angle were facing and the angle of the object

                if(mObject.GetLng() != 0){
                    Location mObjLoc = mObject.GetLocation();
                    double distance = mLocation.distanceTo(mObjLoc);
                    double mObjectRelitiveAngle = (mLocation.getLongitude() - mObjLoc.getLongitude() / (mLocation.getLatitude() - mObjLoc.getLatitude()));

                    double mObjectAngleDiff = mObjectRelitiveAngle + xAngle;

                    double bigX = Math.cos(mObjectAngleDiff + Math.PI) * canvas.getWidth();
                    double bigY = Math.sin(mObjectAngleDiff + Math.PI) * canvas.getHeight();
                    mObject.Radar().SetRadarXY(mObjectAngleDiff, distance);
                    double screenX = bigX + canvas.getWidth()/2;
                    double screenY = Math.sin(yAngle) * canvas.getHeight();
                    //Figure out how wide the angle of view seen by the camera is
                    double viewWidth = Math.PI/2;
                    //First determin if it is in the view range


                   if(bigY > 0 ){
                        AdamObjectHud objHud = mObject.Hud();
                        objHud.SetGoalXY(
                            (int) Math.round(screenX),
                            (int) Math.round(screenY)
                        );
                       objHud.Draw(canvas);
                    }
                    if(
                        (focusObjectId.equals(objId)) ||
                        (focusObjectId == null)
                    ){
                        Log.d("adam", "Deg: " + AdamHelper.To360Degrees(mObjectAngleDiff));
                    }
                }

            }
        }
        ah.Draw(canvas);
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



    public void AddChildControl(AdamDrawable adamDrawable) {
        if(adamDrawable.getId() == null){
            adamDrawable.setId("c" + Integer.toString(controls.size()));
        }
        controls.put(adamDrawable.getId(), adamDrawable);
    }
}
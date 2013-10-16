package com.schematical.adam.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.location.Location;
import android.os.Vibrator;
import android.util.Log;

import android.view.*;

import com.schematical.adam.old.AdamActivityMain;
import com.schematical.adam.model.old.AdamObject;
import com.schematical.adam.sensors.AdamSensorDriver;
import com.schematical.adam.R;
import com.schematical.adam.renderer.Adam2DPoint;
import com.schematical.adam.renderer.Adam3DEngine;
import com.schematical.adam.vmap.AdamVisualMapDriver;
import com.schematical.adam.vmap.drawable.AdamVisualMapDrawable;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user1a on 9/30/13.
 */
public class AdamView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, iAdamDrawable {

    private final AdamObjectMiniProfile miniProfile;
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
    public Bitmap icon;
    public AdamDPad rDPad;
    private boolean targetIsLocked = false;
    private AdamObject objFocused;
    protected AdamVisualMapDrawable visualMap;


    public AdamView(Context context, Camera camera) {
        super(context);
        this.setOnTouchListener(this);
        this.CacheBitmaps();
        mCamera = camera;
        mRadar = new AdamRadar(this);
        mRadar.setTop(20);
        mRadar.setRight(20);
        ah = new AdamHud(this);



        this.InitRightDPad();

        this.miniProfile = new AdamObjectMiniProfile(this);
        this.miniProfile.setTop(20);
        this.miniProfile.setLeft(20);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setWillNotDraw(false);
        InitMapMode();

    }
    private void InitMapMode(){
        visualMap = new AdamVisualMapDrawable(this);
    }
    private void InitRightDPad() {
        rDPad = new AdamDPad(this);
        rDPad.setBottom(20);
        rDPad.setRight(20);

        AdamDPadKey dDown =new AdamDPadKey(this, AdamIcon.rss);
        dDown.setActionParameter("ping");
        dDown.setAction(new AdamAction() {
            public void Exicute(Object actionParameter, AdamDrawable control){
                ((AdamActivityMain)control.getAv().getContext()).Ping();
            }

        });
        rDPad.setDDown(dDown);

        AdamDPadKey dLeft =new AdamDPadKey(this, AdamIcon.target);
        dLeft.setActionParameter("lock_target");
        dLeft.setAction(new AdamAction() {
            public void Exicute(Object actionParameter, AdamDrawable control){
                control.getAv().toggleTargetLock();
            }

        });
        rDPad.setDLeft(dLeft);

        AdamDPadKey dRight =new AdamDPadKey(this, AdamIcon.cancel);
        dRight.setActionParameter("cancel");
        dRight.setAction(new AdamAction() {
            public void Exicute(Object actionParameter, AdamDrawable control){
                control.getAv().toggleTargetLock();
            }

        });
        rDPad.setDRight(dRight);

        AdamDPadKey dUp =new AdamDPadKey(this, AdamIcon.home);
        dUp.setActionParameter("menu");
        dUp.setAction(new AdamAction() {
            public void Exicute(Object actionParameter, AdamDrawable control){
                AdamVisualMapDriver.AddFloorPointAtCurrOrientation();
            }

        });
        rDPad.setDUp(dUp);
    }

    private void toggleTargetLock() {
        this.targetIsLocked = !this.targetIsLocked;
        if(
            (this.targetIsLocked) &&
            (this.objFocused != null)
        ){
            ((AdamActivityMain)this.getContext()).SetStatus("Locking Target: " + this.objFocused.GetAlias());
        }
    }

    public void CacheBitmaps(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = true;
        this.icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Enumeration<String> keys = controls.keys();
        boolean touchEvenFired = false;
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
                touchEvenFired = true;
            }

        }

        if(touchEvenFired){
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

            vibrator.vibrate(200);
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



        AdamActivityMain am = ((AdamActivityMain) getContext());
        AdamObject objNFocused = null;

        if((AdamSensorDriver.getCurrYaw() != null)){
            Location mLocation = am.GetLocation();
            if(mLocation != null){


                Hashtable<String, AdamObject> mObjects = am.GetAdamObjects();
                Enumeration names = mObjects.keys();
                String objId = null;
                double lastFocusXDiff = Double.POSITIVE_INFINITY;
                double currFocusXDiff = 0;
                while(names.hasMoreElements()) {
                    objId = (String) names.nextElement();

                    AdamObject mObject = (AdamObject) mObjects.get(objId);

                    //Find the diff between the Angle were facing and the angle of the object

                    if(mObject.GetLng() != 0){
                        Location mObjLoc = mObject.GetLocation();
                        Adam2DPoint aPoint = Adam3DEngine.Get2DPos(canvas, mObjLoc);



                        mObject.Radar().SetRadarXY(aPoint.getMetaAngle() - AdamSensorDriver.getCurrYaw(), aPoint.getMetaDistance());

                        AdamObjectHud objHud = mObject.Hud();


                        if(aPoint.getTopY() > 0 ){
                            objHud.SetGoalXY(
                                (int) Math.round(aPoint.getX()),
                                (int) Math.round(aPoint.getY()),
                                aPoint.getScale()
                            );
                            if(!this.targetIsLocked){
                                currFocusXDiff = Math.abs(aPoint.getX() - canvas.getWidth()/2);
                                if(currFocusXDiff < lastFocusXDiff){
                                    lastFocusXDiff = currFocusXDiff;
                                    objNFocused = objHud.getAdamObject();
                                }
                            }

                        }else{
                            objHud.Hide();
                        }


                    }

                }
                if(objNFocused != null && !objNFocused.equals(objFocused)){
                    //Trigger highlight effect
                    objFocused = objNFocused;
                }
            }

            //objFocused = ((AdamActivityMain)getContext()).GetAdamObject("100 State");
            Enumeration<String> keys = controls.keys();

            while(keys.hasMoreElements()){
                String key = keys.nextElement();
                AdamDrawable ad = (AdamDrawable) controls.get(key);
                if(ad.getObjParent() == null){
                    ad.Draw(canvas);
                }
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



    public void AddChildControl(AdamDrawable adamDrawable) {
        if(adamDrawable.getId() == null){
            adamDrawable.setId("c" + Integer.toString(controls.size()));
        }
        controls.put(adamDrawable.getId(), adamDrawable);
    }


    public AdamObject GetFocus() {
        return objFocused;
    }

    public void SetFocus(AdamObject objFocused) {
        this.objFocused = objFocused;
    }
    public String GetFocusedAlias(){
        if(this.objFocused != null){
            return this.objFocused.GetAlias();
        }
        return null;
    }
}
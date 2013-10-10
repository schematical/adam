package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.view.MotionEvent;
import android.view.View;

import com.schematical.adam.AdamActivityMain;
import com.schematical.adam.AdamObject;
import com.schematical.adam.AdamSensorDriver;
import com.schematical.adam.AdamView;
import com.schematical.adam.drawable.AdamDrawable;
import com.schematical.adam.drawable.opengl.AdamOpenGLIcon;
import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.renderer.Adam2DPoint;
import com.schematical.adam.renderer.Adam3DEngine;
import com.schematical.adam.vmap.AdamVisualMapDriver;
import com.schematical.adam.vmap.AdamVisualMapPoint;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamRadar extends AdamDrawable {
    public static final double DEFAULT_MAX_DIST = 100;
    private final AdamStackablePercentField txtAccuracy;
    private AdamIcon cursor;


    protected boolean blnFullScreen = false;
    protected int orig_height = 0;
    protected int orig_width = 0;
    public AdamRadar(iAdamDrawable nAv) {
        super(nAv);
        txtAccuracy = new AdamStackablePercentField(this);
        txtAccuracy.setName("Accuracy");
        txtAccuracy.setWidth(this.getWidth());
        try {
            Method m = AdamRadar.class.getMethod("GetAccuracy");
            txtAccuracy.Follow(this, m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        cursor = new AdamIcon(this, AdamIcon.bullseye);
        cursor.setWidth(50);
        cursor.setHeight(50);
    }
    public Double GetAccuracy(){
        Location objLocation = ((AdamActivityMain)av.getContext()).GetLocation();
        if(objLocation == null){
            return 0d;
        }
        return ((Float)(1/objLocation.getAccuracy())).doubleValue();
    }
    public void ToggleFullScreen(){
        if(this.blnFullScreen){
            this.blnFullScreen = false;
            width = orig_width;
            height = orig_height;
        }else{
            this.blnFullScreen = true;
            orig_height = this.height;
            orig_width = this.width;
            width = av.GetCanvas().getHeight()- (2 * padding);
            height = av.GetCanvas().getHeight() - (2 * padding);
        }
    }

    public void Draw(Canvas canvas){

        int middleX = this.getX() + width/2 ;
        int middleY = this.getY() + width/2;

        canvas.drawCircle(
                middleX,
                middleY,
                height/2,
                bg_paint
        );

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(
                middleX,
                middleY,
                10,
                paint
        );

        canvas.drawCircle(
                middleX,
                middleY,
                height/2,
                paint
        );

        paint.setStyle(Paint.Style.FILL);

        Double yaw = AdamSensorDriver.getCurrYaw();

        for(int i = 0; i < 4; i ++){
            Double offset = Math.PI * i/2;
            canvas.drawText(
                ((Long)Math.round((offset)/Math.PI*180)).toString() ,
                Math.round(middleX + Math.cos(-1 * yaw + offset) * width/2),
                Math.round(middleY + Math.sin(-1 * yaw + offset) * height/2),
                paint
            );
        }
        DrawAdamObjects(canvas, width, height);
        DrawStackable(canvas);
        DrawCursor(canvas);

    }
    public void DrawCursor(Canvas canvas){
        //For now lets just draw a an icon at the distance point
        AdamVisualMapPoint ap = AdamVisualMapDriver.GetLocationOfFloorPoint();

        //Log.d("adam", "x:" + ap.getX() + "  y: " + ap.getY() + " z: " + ap.getZ());
        Location rp = ap.getGeoLocation(AdamLocation.GetLocation());

        Adam2DPoint a2dPoint = Adam3DEngine.Get2DPos(canvas, rp);

        //Log.d("adam", a2dPoint.getX() + "/" + a2dPoint.getY());
        Double yaw = AdamSensorDriver.getCurrYaw();
        double radius = a2dPoint.getMetaDistance() / AdamRadar.DEFAULT_MAX_DIST * (this.height/2);
        Long bigX  = Math.round( (Math.cos(a2dPoint.getMetaAngle() - yaw) *  radius)+ getCenterX()- cursor.width/2);
        Long bigY =  Math.round( (Math.sin(a2dPoint.getMetaAngle()- yaw)  * radius) + getCenterY()- cursor.height/2);
        cursor.setLeft(bigX.intValue());
        cursor.setTop(bigY.intValue());
        //cursor.setHeight(((Long)Math.round(a2dPoint.getScale() * 140)).intValue());
        cursor.Draw(canvas);
    }
    public Integer getCenterX(){
        return this.getX() + (this.getWidth()/2);
    }
    public Integer getCenterY(){
        return this.getY() + (this.getHeight()/2);
    }
    public void DrawStackable(Canvas canvas){
        Enumeration<String> keys = children.keys();
        int intNextY = getY() + this.getHeight() + this.padding;
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamDrawable ad =  this.children.get(key);

            if(ad instanceof AdamStackable){
                ad.setTop(intNextY);
                ad.setLeft(this.getX());
                ad.Draw(canvas);
                intNextY += ad.getHeight() + this.padding;
            }
        }
    }
    public void DrawAdamObjects(Canvas canvas, int nWidth, int nHeight){
        Hashtable<String, AdamObject> aObjects = ((AdamActivityMain)av.getContext()).GetAdamObjects();
        Enumeration<String> keys = aObjects.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamObject ao = (AdamObject) aObjects.get(key);
            ao.Radar().Draw(canvas, this);

        }
    }
    public boolean isFullScreen() {
        return blnFullScreen;
    }
    public void onTouch(View v, MotionEvent event) {
        this.ToggleFullScreen();
    }



}

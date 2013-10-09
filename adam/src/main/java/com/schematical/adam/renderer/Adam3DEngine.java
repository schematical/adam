package com.schematical.adam.renderer;

import android.graphics.Canvas;
import android.location.Location;
import android.util.Log;

import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.AdamSensorDriver;

import java.util.Hashtable;

/**
 * Created by user1a on 10/8/13.
 */
public class Adam3DEngine {

    public static Adam2DPoint Get2DPos(Canvas canvas, Location mObjLoc){
        return Get2DPos(canvas, mObjLoc, AdamLocation.GetLocation());
    }
    public static Adam2DPoint Get2DPos(Canvas canvas, Location mObjLoc, Location mLocation){

        double distance = mLocation.distanceTo(mObjLoc);
        double alt_diff = mLocation.getAltitude() - mObjLoc.getAltitude();
        /*mLocation.removeBearing();
        mObjLoc.removeBearing();
        double baring = (mLocation.bearingTo(mObjLoc)/180) * Math.PI;*/
        double yDiff = mObjLoc.getLongitude() - mLocation.getLongitude();
        double xDiff = mObjLoc.getLatitude() - mLocation.getLatitude();
        Hashtable<String, Double> mLocMeters = AdamLocation.GetXYMeters(mLocation.getLatitude(), mLocation.getLongitude());
        Hashtable<String, Double> objLocMeters = AdamLocation.GetXYMeters(mObjLoc.getLatitude(), mObjLoc.getLongitude());
        double xDiffMeters = ((Double)mLocMeters.get("x")) - ((Double)objLocMeters.get("x"));
        double yDiffMeters = ((Double)mLocMeters.get("y")) - ((Double)objLocMeters.get("y"));
        double baring = Math.atan(yDiffMeters/xDiffMeters);


        double yaw = AdamSensorDriver.getCurrYaw();
        double mObjectAngleDiff = baring - yaw;
        Log.d("adam", "Yaw: " + (yaw / Math.PI * 180) + "Pointer Baring:" + (baring / Math.PI * 180) + " Diff: " + (mObjectAngleDiff / Math.PI * 180));
        double bigX = Math.cos(mObjectAngleDiff) * canvas.getWidth()/2;
        double nYAngle = alt_diff/distance  + AdamSensorDriver.getCurrPitch();
        double screenX = bigX + canvas.getWidth()/2;


        double screenY = (-1 * (Math.sin(nYAngle) * canvas.getHeight()/2)) + (canvas.getHeight()/2);
        Adam2DPoint ap = new Adam2DPoint(screenX,screenY, 1/Math.sqrt(distance));

        //Calc view from top
        double topX  = Math.cos(mObjectAngleDiff) * distance;
        double topY = Math.sin(mObjectAngleDiff) * distance;
        ap.setTopX(topX);
        ap.setTopY(topY);
        ap.setMetaAngle(baring);
        ap.setMetaDistance(distance);
        return ap;
    }
}

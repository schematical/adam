package com.schematical.adam.renderer;

import android.graphics.Canvas;
import android.location.Location;
import android.util.Log;

import com.schematical.adam.drawable.AdamRadar;
import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.AdamSensorDriver;

import java.util.Hashtable;

/**
 * Created by user1a on 10/8/13.
 */
public class Adam3DEngine {
    public static Double zoom = .1d;
    public static Adam2DPoint Get2DPos(Canvas canvas, Location mObjLoc){
        return Get2DPos(canvas, mObjLoc, AdamLocation.GetLocation());
    }
    public static Adam2DPoint Get2DPos(Canvas canvas, Location mObjLoc, Location mLocation){

        double distance = mLocation.distanceTo(mObjLoc);
        double scale = 1/(distance * zoom);
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


        double baring = AdamLocation.distFrom(
            mLocMeters.get("x").floatValue(),
            mLocMeters.get("y").floatValue(),
            objLocMeters.get("x").floatValue(),
            objLocMeters.get("y").floatValue()
        );
        double yaw = AdamSensorDriver.getCurrYaw();
        double bigX =(Math.cos(baring + yaw) *  (canvas.getWidth()/2)) /scale;
        double nYAngle = alt_diff/distance  + AdamSensorDriver.getCurrPitch();
        double screenX = bigX + canvas.getWidth()/2;

        double bigY = (-1 * (Math.sin(nYAngle) * (canvas.getHeight()/2)))  /scale;
        Log.d("adam", ((Double)bigX) + "/" + ((Double)bigY));

        double screenY = bigY + (canvas.getHeight()/2);
        Adam2DPoint ap = new Adam2DPoint(screenX,screenY, scale);

        //Calc view from top
        double topX  = Math.cos(baring + yaw) * distance;
        double topY = Math.sin(baring + yaw) * distance;
        ap.setTopX(topX);
        ap.setTopY(topY);
        ap.setMetaAngle(baring + yaw);
        ap.setMetaDistance(distance);
        return ap;
    }
}

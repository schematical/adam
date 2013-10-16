package com.schematical.adam.renderer;

import android.graphics.Canvas;
import android.location.Location;
import android.util.Log;

import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.sensors.AdamSensorDriver;

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
        double scale = 1;//1/Math.sqrt(distance) * zoom;
        double alt_diff = mLocation.getAltitude() - mObjLoc.getAltitude();

        Hashtable<String, Double> mLocMeters = AdamLocation.GetXYMeters(mLocation.getLatitude(), mLocation.getLongitude());
        Hashtable<String, Double> objLocMeters = AdamLocation.GetXYMeters(mObjLoc.getLatitude(), mObjLoc.getLongitude());


        Double baring = AdamLocation.GetBearing(
            mLocMeters.get("x"),
            mLocMeters.get("y"),
            objLocMeters.get("x"),
            objLocMeters.get("y")
        );
        Double baringZ = AdamLocation.GetBearing(
            mLocMeters.get("x"),
            mLocation.getAltitude(),
            objLocMeters.get("x"),
            mObjLoc.getAltitude()
        );
        double yaw = AdamSensorDriver.getCurrYaw();
        double bigX =(Math.sin(baring + yaw) *  (canvas.getWidth()/2)) /scale;

        double screenX = bigX + canvas.getWidth()/2;

        double bigY = (Math.sin(baringZ  - AdamSensorDriver.getCurrPitch()) * (canvas.getHeight()/2))  /scale;
        Log.d("adam", (baring/Math.PI*180 + " - " + (Double)bigX) + "/" + ((Double)bigY) + " - " + 1/scale);

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

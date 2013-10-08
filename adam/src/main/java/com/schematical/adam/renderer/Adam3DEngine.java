package com.schematical.adam.renderer;

import android.graphics.Canvas;
import android.location.Location;

import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.AdamSensorDriver;

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
        double baring = mLocation.bearingTo(mObjLoc);
        double mObjectRelitiveAngle = (baring/180) * Math.PI;


        double mObjectAngleDiff = mObjectRelitiveAngle + AdamSensorDriver.getCurrYaw();

        double bigX = Math.cos(mObjectAngleDiff + Math.PI) * canvas.getWidth()/2;
        //double bigY = Math.sin(mObjectAngleDiff + Math.PI) * canvas.getHeight();

        double nYAngle = alt_diff/distance  + AdamSensorDriver.getCurrPitch();
        double screenX = bigX + canvas.getWidth()/2;
        double screenY = ((Math.sin(nYAngle) * canvas.getHeight()/2)) + (canvas.getHeight()/2);
        Adam2DPoint ap = new Adam2DPoint(screenX,screenY, 1/distance);
        ap.setMetaAngle(mObjectAngleDiff);
        ap.setMetaDistance(distance);
        return ap;
    }
}

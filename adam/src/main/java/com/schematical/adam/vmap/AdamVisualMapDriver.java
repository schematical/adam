package com.schematical.adam.vmap;

import android.location.Location;
import android.util.Log;

import com.schematical.adam.AdamSensorDriver;

/**
 * Created by user1a on 10/8/13.
 */
public class AdamVisualMapDriver {
    public static final Double DEFAULT_EYE_HEIGHT = 1.6256;


    public static AdamVisualMapPoint floorAp;
    public static AdamVisualMap map;
    public static AdamVisualMap InitMap(){
        map = new AdamVisualMap();
        //floorAp = new AdamVisualMapPoint(0d,0d,0d);
        return map;
    }
    public static Double GetEstimatedDistanceToFloorPoint(){

        Double distX = DEFAULT_EYE_HEIGHT * Math.tan((Math.PI/2)-AdamSensorDriver.getCurrPitch());

        return distX;
    }
    public static AdamVisualMapPoint GetLocationOfFloorPoint(){
        Double distX = GetEstimatedDistanceToFloorPoint();//Distance to floor point in meters
        Double cZ = -1 * DEFAULT_EYE_HEIGHT;//The distance from the floor point is always the eye height below the measuring device

        Double yaw = AdamSensorDriver.getCurrYaw();
        Double cX = Math.cos(yaw) * distX;
        Double cY = Math.sin(yaw) * distX;

        floorAp = new AdamVisualMapPoint(cX, cY, cZ);
       return floorAp;
    }
    public static void AddFloorPointAtCurrOrientation(){
        AdamVisualMapPoint ap = GetLocationOfFloorPoint();
        map.AddPoint(ap);
    }
    public static AdamVisualMap getMap() {
        return map;
    }
}

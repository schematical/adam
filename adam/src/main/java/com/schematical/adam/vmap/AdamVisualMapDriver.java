package com.schematical.adam.vmap;

import android.location.Location;
import android.util.Log;

import com.schematical.adam.AdamSensorDriver;

/**
 * Created by user1a on 10/8/13.
 */
public class AdamVisualMapDriver {
    public static final Double DEFAULT_EYE_HEIGHT = 1.6256;



    public static AdamVisualMap map;
    public static AdamVisualMap InitMap(){
        map = new AdamVisualMap();
        return map;
    }
    public static Double GetEstimatedDistanceToFloorPoint(){
        Log.d("adam", "Pitch:" + ((Math.PI/2)-AdamSensorDriver.getCurrPitch() /Math.PI * 180));
        Double distX = DEFAULT_EYE_HEIGHT * Math.tan((Math.PI/2)-AdamSensorDriver.getCurrPitch());
        return distX;
    }
    public static AdamVisualMapPoint GetLocationOfFloorPoint(){
        Double distX = GetEstimatedDistanceToFloorPoint();//Distance to floor point in meters
        Double cZ = -1 * DEFAULT_EYE_HEIGHT;//The distance from the floor point is always the eye height below the measuring device

        Double yaw = AdamSensorDriver.getCurrYaw();
        Double cY = Math.cos(yaw) * distX;
        Double cX = Math.sin(yaw) * distX;

        AdamVisualMapPoint ap = new AdamVisualMapPoint(
            cX,
            cY,
            cZ
        );
       return ap;
    }
    public static void AddFloorPointAtCurrOrientation(){
        AdamVisualMapPoint ap = GetLocationOfFloorPoint();
        map.AddPoint(ap);
    }
    public static AdamVisualMap getMap() {
        return map;
    }
}

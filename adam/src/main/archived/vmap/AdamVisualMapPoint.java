package com.schematical.adam.vmap;

import android.location.Location;

import com.schematical.adam.location.AdamLocationDriver;

import java.util.Hashtable;

/**
 * Created by user1a on 10/8/13.
 */
public class AdamVisualMapPoint {
    //This should always be stored as 0 is north. X I think...
    protected Location referenceLocation;


    protected Double x;
    protected Double y;
    protected Double z;
    private AdamVisualMap map;

    public AdamVisualMapPoint(Double nX, Double nY, Double nZ) {
        x = nX;
        y = nY;
        z = nZ;
    }
    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public void setMap(AdamVisualMap map) {
        this.map = map;
    }

    public Location getGeoLocation(Location location) {
        Hashtable<String, Double> coords = AdamLocationDriver.GetXYMeters(
                location.getLatitude(),
                location.getLongitude()
        );
        Double gX = (Double)coords.get("x");
        Double gY = (Double)coords.get("y");
        Double realX = x + gX;
        Double realY = y + gY;
        Double realZ = z + location.getAltitude();

        Location rLocation = AdamLocationDriver.GetGeoLocationFromMetersXY(realX, realY);
        rLocation.setAccuracy(location.getAccuracy());
        rLocation.setAltitude(realZ);
        return rLocation;
    }
    public Location getGeoLocation() {
       return getGeoLocation(this.referenceLocation);
    }

    public void updateCoords(Double cX, Double cY, Double cZ) {
        double weight = 100;
        x = ((x * weight) + cX) / (weight+1);
        y = ((y * weight) + cY) / (weight+1);
        z = ((z * weight) + cX) / (weight+1);
    }
}

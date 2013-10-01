package com.schematical.adam;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamPing {
    public double pingStrength;
    public double pingLat;
    public double pingLng;
    public double pingElev;
    public double pingAccuracy;
    AdamPing(double strength, double lat, double lng, double elev, double accuracy){
        pingStrength = strength;
        pingLat = lat;
        pingLng = lng;
        pingElev = elev;
        pingAccuracy = accuracy;
    }
}

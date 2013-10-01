package com.schematical.adam;

import java.sql.Timestamp;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamPing {
    public static final double METERS_DMB = 3/10;
    public double pingStrength;
    public double pingLat;
    public double pingLng;
    public double pingElev;
    public double pingAccuracy;
    public Timestamp pingTimestamp;
    AdamPing(double strength, double lat, double lng, double elev, double accuracy, Timestamp ts){
        pingStrength = strength;
        pingLat = lat;
        pingLng = lng;
        pingElev = elev;
        pingAccuracy = accuracy;
        pingTimestamp = ts;
    }
    public double DistanceFrom(AdamPing p2){
        return distFrom(
            pingLat,
            pingLng,
            p2.pingLat,
            p2.pingLng
        );
    }
    public double AngleInRelationTo(AdamPing p2){
        double o = (this.pingLat - p2.pingLat);
        double a = (this.pingLng - p2.pingLng);
        return Math.atan(o/a);
    }
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return new Float(dist * meterConversion).floatValue();
    }
}

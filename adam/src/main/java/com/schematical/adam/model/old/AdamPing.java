package com.schematical.adam.model.old;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamPing {
    public static final double METERS_DMB = 3/10;
    private final double pingFrequency;
    public double pingStrength;
    public double pingLat;
    public double pingLng;
    public double pingElev;
    public double pingAccuracy;
    public String pingType;
    public long pingTimestamp;
    AdamPing( double lat, double lng, double elev, double strength, double frequency, double accuracy, long ts){

        pingLat = lat;
        pingLng = lng;
        pingElev = elev;
        pingStrength = strength;
        pingFrequency = frequency;

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
        if(a == 0){
            return Math.sin(0);
        }
        double angle = Math.atan(o/a);
        return angle;
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
    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("pingLat", this.pingLat);
            jObj.put("pingLng", this.pingLng);
            jObj.put("pingElev", this.pingElev);
            jObj.put("pingAccuracy", this.pingAccuracy);
            jObj.put("pingStrength", this.pingStrength);
            jObj.put("pingFrequency", this.pingFrequency);
            jObj.put("pingTimestamp", this.pingTimestamp);
            jObj.put("pingType", this.pingType);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jObj;

    }
}

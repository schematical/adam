package com.schematical.adam.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.schematical.adam.AdamActivityMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by user1a on 10/4/13.
 */
public class AdamLocation  implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    protected static double lat_t = 0;
    protected static double lng_t = 0;
    protected static double altitude_t = 0;
    protected static double accuracy_t = 0;
    protected static double weight_t = 0;
    protected static double measure_ct = 0;

    protected AdamActivityMain am;



    public AdamLocation(AdamActivityMain adamActivityMain) {
        am = adamActivityMain;
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) am.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        am.SetStatus("Waiting for GPS");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    public static Location GetLocation(){
        Location nLocation = new Location("gps");
        if(lat_t ==0){
            nLocation.setLatitude(43.074785782963);
            nLocation.setLongitude(-89.38710576539);
            nLocation.setAltitude(0);
            nLocation.setAccuracy(Float.POSITIVE_INFINITY);
            return nLocation;
        }

        nLocation.setLatitude(lat_t / weight_t);
        nLocation.setLongitude(lng_t / weight_t);
        nLocation.setAltitude(altitude_t/weight_t);
        nLocation.setAccuracy(Math.round(1/(weight_t/measure_ct)));

        return nLocation;
    }
    public static Double GetBearing(Double fromX, Double fromY, Double toX, Double toY){
        Double diffX = fromX - toX;
        Double diffY = fromY - toY;
        Double hyp = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
        Double rot = Math.acos(diffX/hyp);

        if(diffY < 0 ){
            rot += Math.PI;
        }
        return rot;

    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Adam", "CONNECTED!");
    }

    @Override
    public void onDisconnected() {
        Log.d("Adam", "DISCONNECTED!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Adam", "Connection Failed :(");
    }






    public static Hashtable<String, Double> GetXYMeters(Double lat, Double lng){
        //$latitudeCircumference = 40075160 * cos($fltLat/180*pi());
        Double latitudeCircumference = 40075160 * Math.cos(lat/180*Math.PI);

        //$resultX = $fltLong * $latitudeCircumference / 360;
        Double resultX = lng * latitudeCircumference / 360;

        //$resultY = $fltLat * 40008000 / 360;
        Double resultY = lat * 40008000 / 360;
        Hashtable<String, Double> rData = new Hashtable<String, Double>();
        rData.put("x", resultX);
        rData.put("y", resultY);
        return rData;
    }
    public static Location GetGeoLocationFromMetersXY(Double xMeters, Double yMeters){

        Double fltLat =  yMeters / (40008000 / 360);
        Double latitudeCircumference = 40075160 * Math.cos(fltLat / 180 * Math.PI);

        Double fltLong = xMeters/(latitudeCircumference / 360);
        Location rLocation = new Location("gps");
        rLocation.setLatitude(fltLat);
        rLocation.setLongitude(fltLong);
        return rLocation;
    }

    public static float distFrom(Float lat1, Float lng1, Float lat2, Float lng2) {
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
    public void onLocationChanged(Location location) {

        Location oLoc = this.GetLocation();


        double weight = 1/location.getAccuracy();
        this.lat_t += location.getLatitude() * weight;
        this.lng_t += location.getLongitude() * weight;
        this.altitude_t += location.getAltitude() * weight;
        this.weight_t += weight;
        this.measure_ct += 1;
        if(oLoc == null){
            am.SetStatus("Location Updated");
            this.UpdateServer();
        }


    }
    public static void UpdateServer(){
        Location l = GetLocation();
        JSONObject jo = new JSONObject();
        try {
            jo.put("event", "location_update");
            jo.put("lat", l.getLatitude());
            jo.put("lng", l.getLongitude());
            jo.put("alt", l.getAltitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AdamActivityMain.SendToServer(jo);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {
        am.SetStatus("GPS Enabled");
    }

    public void onProviderDisabled(String provider) {
        am.SetStatus("GPS Disabled");
    }

}

package com.schematical.adam;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

import java.util.Hashtable;

/**
 * Created by user1a on 10/4/13.
 */
public class AdamLocation  implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    protected double lat_t = 0;
    protected double lng_t = 0;
    protected double altitude_t = 0;
    protected double accuracy_t = 0;
    protected double weight_t = 0;
    protected double measure_ct = 0;

    protected AdamActivityMain am;
    private Location mLocation;
    private Location mLastPingLocation;
    public Location mOrigLocation;

    public AdamLocation(AdamActivityMain adamActivityMain) {
        am = adamActivityMain;
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) am.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        am.SetStatus("Waiting for GPS");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    public Location GetLocation(){
        mLocation = new Location("gps");
        mLocation.setLatitude(this.lat_t / this.weight_t);
        mLocation.setLongitude(this.lng_t / this.weight_t);
        mLocation.setAltitude(this.altitude_t/this.weight_t);
        mLocation.setAccuracy(Math.round(1/(this.weight_t/this.measure_ct)));

        return mLocation;
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
    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
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
        mLocation = location;


        double weight = 1/location.getAccuracy();
        this.lat_t += location.getLatitude() * weight;
        this.lng_t += location.getLongitude() * weight;
        this.altitude_t += location.getAltitude() * weight;
        this.weight_t += weight;
        this.measure_ct += 1;
        //this.accuracy_t += location.getAccuracy();


        if(
                (mLastPingLocation == null) ||
                (mLastPingLocation.distanceTo(mLocation) > mLocation.getAccuracy() * (1.5))
        ){
            if(mOrigLocation == null){
                mOrigLocation = mLocation;
                am.bluetoothDriver.StartDiscovery();
            }
            mLastPingLocation = mLocation;
            am.allowPing = true;

        }


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

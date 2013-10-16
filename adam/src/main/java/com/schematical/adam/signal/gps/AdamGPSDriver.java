package com.schematical.adam.signal.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.schematical.adam.AdamWorldActivity;
import com.schematical.adam.signal.iAdamSignalDriver;

/**
 * Created by user1a on 10/16/13.
 */
public class AdamGPSDriver implements iAdamSignalDriver, LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
    public AdamGPSDriver(){
        AdamWorldActivity am = AdamWorldActivity.getInstance();
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) am.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        //am.SetStatus("Waiting for GPS");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    public void Connect(){}
    public void Disconnect(){}
    public void onLocationChanged(Location location) {




    }
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {
        //AdamWorldActivity.SetStatus("GPS Enabled");
    }

    public void onProviderDisabled(String provider) {
        //AdamWorldActivity.SetStatus("GPS Disabled");
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

}

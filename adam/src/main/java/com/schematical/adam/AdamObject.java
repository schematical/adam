package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.util.Log;

import com.schematical.adam.drawable.AdamObjectHud;
import com.schematical.adam.drawable.AdamObjectRadar;
import com.schematical.adam.drawable.AdamRadar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamObject {

    protected AdamActivityMain am;

    protected String id;
    protected String aId;
    protected AdamObjectRadar mRadar;
    protected AdamObjectHud mHud;


    protected String alias;


    protected double lat;
    protected double lng;
    protected double altutide;

    protected List<AdamPing> pings;
    private double accuracy;
    public double radarAngle = 0;
    public double radarDistance = 0;

    AdamObject(AdamActivityMain nAm, String nId){
        am = nAm;
        id = nId;
        pings  = new ArrayList<AdamPing>();


    }
    public AdamObjectHud Hud(){
        if(this.mHud == null){
            this.mHud = new AdamObjectHud(am.GetView(), this);
        }
        return mHud;
    }
    public String GetId(){
        return id;
    }
    public boolean IsFocused(){
        return am.mView.focusObjectId.equals(this.id);
    }


    public double GetLat(){
        return lat;
    }
    public double GetLng(){
        return lng;
    }
    public double GetAltitude(){
        return altutide;
    }
    public void Update(Object data){
        java.util.Date date= new java.util.Date();
        if(data instanceof JSONObject){
            try{
                JSONObject jObj = (JSONObject) data;
                this.id = (String)jObj.get("id");
                if(jObj.has("alias")){
                    this.alias = (String)jObj.get("alias");
                }
                if(jObj.has("lat") && jObj.has("lng")){
                    this.lat = (Double)jObj.get("lat");
                    this.lng = (Double)jObj.get("lng");
                }
                if(jObj.has("target")){

                    if(!jObj.get("target").equals(null)){
                        JSONObject target = (JSONObject)jObj.get("target");
                        this.lat = (Double)target.get("lat");
                        this.lng = (Double)target.get("lng");
                        this.accuracy = (Double) target.get("accuracy");
                    }
                }
                Object qPing = jObj.get("pings");
                if(qPing instanceof JSONObject){
                    JSONObject jPings = (JSONObject) qPing;
                    Iterator keys = jPings.keys();
                    while(keys.hasNext()){
                        JSONObject jPingData = (JSONObject) jPings.get((String) keys.next());
                        double pingLat = (Double)jPingData.get("pingLat");
                        double pingLng = 0d;
                        if(jPingData.has("pintLng")){
                            pingLng = (Double)jPingData.get("pintLng");
                        }else{
                            pingLng = (Double)jPingData.get("pingLng");
                        }
                        double pingElev = (Integer) jPingData.get("pingElev");
                        double pingAccuracy =  (Integer) jPingData.get("pingAccuracy");
                        double pingStrength = (Integer)   jPingData.get("pingStrength");
                        double pingFrequency = (Integer)   jPingData.get("pingFrequency");
                        long pingTimestamp = (Long) jPingData.get("pingTimestamp");
                        AdamPing objPing = new AdamPing(
                            pingLat,
                            pingLng,
                            pingElev,
                            pingStrength,
                            pingFrequency,
                            pingAccuracy,
                            pingTimestamp
                        );
                        pings.add(objPing);
                    }
                }
            }catch(JSONException e){
                Log.d("adam", "ADAMERROR:!!!! " + e.getMessage());
                e.printStackTrace();
            }
        }
        if(data instanceof AdamBluetoothScanResult){
            AdamBluetoothScanResult sr = (AdamBluetoothScanResult) data;
            Location objLocation = am.GetLocation();
            AdamPing ap = new AdamPing(
                    objLocation.getLatitude(),
                    objLocation.getLongitude(),
                    objLocation.getAltitude(),
                    sr.rssi,
                    0,
                    objLocation.getAccuracy(),
                    date.getTime()
            );
            this.alias = sr.name;
            ap.pingType = AdamBluetooth.TYPE;
            pings.add(ap);
            //UpdateLatLng();
        }
        if(data instanceof ScanResult){
            //Track in a list of pings
            ScanResult sr = (ScanResult) data;
            this.alias = sr.SSID;
            Location objLocation = am.GetLocation();
            if(objLocation != null){
                AdamPing ap = new AdamPing(
                        objLocation.getLatitude(),
                        objLocation.getLongitude(),
                        objLocation.getAltitude(),
                        sr.level,
                        sr.frequency,
                        objLocation.getAccuracy(),
                        date.getTime()
                );
                pings.add(ap);

            }
        }
    }


    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("id", this.id);
            jObj.put("alias", this.alias);

            JSONObject jPings = new JSONObject();

            for(int i = 0; i < this.pings.size(); i++){
                jPings.put(Integer.toString(i), this.pings.get(i).toJSONObject());

            }
            jObj.put("pings", jPings);


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jObj;


    }
    public void ClearPings(){
        this.pings = new ArrayList<AdamPing>();
    }
    public String GetAlias() {
        return alias;
    }


    public AdamObjectRadar Radar() {
        if(mRadar == null){
            this.mRadar = new AdamObjectRadar(am.GetView(), this);
        }
        return mRadar;
    }
    public Location GetLocation(){
        Location nLocation = new Location("gps");
        nLocation.setAccuracy((int)this.accuracy);
        nLocation.setLatitude(this.lat);
        nLocation.setLongitude(this.lng);
        nLocation.setAltitude(this.altutide);
        return nLocation;
    }
}

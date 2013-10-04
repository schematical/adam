package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamObject {

    protected AdamActivityMain am;
    private final Paint paint;
    protected String id;
    protected String aId;
    protected String alias;
    protected int goalX = 0;
    protected int goalY = 0;
    protected int currX = 0;
    protected int currY = 0;

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

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(50);
    }
    public String GetId(){
        return id;
    }
    public boolean IsFocused(){
        return am.mPreview.focusObjectId.equals(this.id);
    }
    public void Draw(Canvas canvas){
        currX = Math.round(currX + goalX)/2;
        currY = Math.round(currY + goalY)/2;
        canvas.drawText(
                this.id,
                currX,
                currY,
                paint
        );
        if(IsFocused()){
            Location newLocation = new Location("gps");
            Location oLocation = am.GetLocation();
            newLocation.setLatitude(this.lat);
            newLocation.setLongitude(this.lng);
            newLocation.setAltitude(oLocation.getAltitude());

            newLocation.setAccuracy(3.0f);
            float cDist = oLocation.distanceTo(newLocation);
            canvas.drawText(
                    "Dist:" + Float.toString(cDist),
                    currX,
                    currY + 45,
                    paint
            );
        }
    }
    public void SetGoalXY(int nGoalX, int nGoalY){
        goalX = nGoalX;
        goalY = nGoalY;
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

    public void SetRadarXY(double nRadarAngle, double nDistance) {
        this.radarAngle = nRadarAngle;
        this.radarDistance = nDistance;
    }
    public void DrawRadar(Canvas canvas, AdamRadar adamRadar, int nWidth, int nHeight){
        paint.setTextSize(20);
        double bigX  = Math.cos(this.radarAngle) * this.radarDistance / AdamRadar.DEFAULT_MAX_DIST * (nWidth/2) ;
        double bigY = Math.sin(this.radarAngle)  * this.radarDistance / AdamRadar.DEFAULT_MAX_DIST * (nHeight/2);

        String sDesc = this.alias + "(" + Double.toString(this.lat) + "," + Double.toString(this.lng) + ")";
        canvas.drawText(
            sDesc,
            (float) bigX + canvas.getWidth() / 2,
            (float)bigY + canvas.getHeight() / 2,
            paint
        );
    }
}

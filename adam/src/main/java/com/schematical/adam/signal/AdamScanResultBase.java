package com.schematical.adam.signal;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;

/**
 * Created by user1a on 10/16/13.
 */
public class AdamScanResultBase {


    protected String alias;
    protected String id;
    protected int rssi;
    protected String type;
    protected String extra;
    protected int frequency;
    protected long timestamp;
    public AdamScanResultBase(){
        timestamp = System.currentTimeMillis();
    }
    public String getAlias() {
        return alias;
    }

    public String getId() {
        return id;
    }

    public int getRssi() {
        return rssi;
    }

    public String getType() {
        return type;
    }

    public String getExtra() {
        return extra;
    }

    public int getFrequency() {
        return frequency;
    }
    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("id", this.id);
            jObj.put("rssi", this.rssi);
            jObj.put("frequency", this.frequency);
            jObj.put("timestamp", this.timestamp);
            jObj.put("type", this.type);
            jObj.put("alias", this.alias);
            jObj.put("extra", this.extra);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jObj;

    }
}

package com.schematical.adam;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamObject {
    protected String id;
    AdamObject(){
        id = "test";
    }
    public String GetId(){
        return id;
    }
    public double GetLat(){
        return 0.1;
    }
    public double GetLng(){
        return 0.1;
    }
    public double GetAltitude(){
        return 0.1;
    }
}

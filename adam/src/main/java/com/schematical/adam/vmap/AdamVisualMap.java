package com.schematical.adam.vmap;

import java.util.ArrayList;

/**
 * Created by user1a on 10/8/13.
 */
public class AdamVisualMap {


    protected ArrayList<AdamVisualMapPoint> points = new ArrayList<AdamVisualMapPoint>();
    public void AddPoint(AdamVisualMapPoint point){
        point.setMap(this);
        points.add(point);
    }
    public ArrayList<AdamVisualMapPoint> getPoints() {
        return points;
    }
}

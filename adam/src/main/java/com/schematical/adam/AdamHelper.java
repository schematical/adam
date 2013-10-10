package com.schematical.adam;

/**
 * Created by user1a on 10/5/13.
 */
public class AdamHelper {
    public static int To360Degrees(double rot){
        return (int)Math.round(rot/Math.PI * 180);
    }

}

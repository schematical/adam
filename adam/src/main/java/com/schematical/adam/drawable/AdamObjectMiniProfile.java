package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.location.Location;

import com.schematical.adam.old.AdamActivityMain;
import com.schematical.adam.signal.old.AdamObject;

import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamObjectMiniProfile  extends AdamDrawable{
    public static final int MAIN_WINDOW_WIDTH = 150;
    public static final int MAIN_WINDOW_HEIGHT = 150;
    private final AdamStackableTextField txtFocus;
    private final AdamStackablePercentField txtDistance;
    private final AdamStackablePercentField txtAccuracy;

    protected AdamStackableTextField txtStatus;

    public AdamObjectMiniProfile(iAdamDrawable nAv){//, AdamObject nAdamObject) {
        super(nAv);
        //adamObject = nAdamObject;
        txtStatus = new AdamStackableTextField(this);
        try {
            Method m = AdamActivityMain.class.getMethod("GetStatus");
            txtStatus.Follow(((AdamActivityMain) av.getContext()), m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }



        txtFocus = new AdamStackableTextField(this);
        txtFocus.Focus();
        try {
            Method m = AdamView.class.getMethod("GetFocusedAlias");
            txtFocus.Follow(av, m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        txtDistance = new AdamStackablePercentField(this);
        txtDistance.setName("Distance");
        txtDistance.setUnits("m");
        try {
            Method m = AdamObjectMiniProfile.class.getMethod("GetDistanceToFocusObject");
            txtDistance.Follow(this, m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        txtAccuracy = new AdamStackablePercentField(this);
        txtAccuracy.setName("Accuracy");
        try {
            Method m = AdamObjectMiniProfile.class.getMethod("GetFocusObjectAccuracy");
            txtAccuracy.Follow(this, m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
    public Double GetFocusObjectAccuracy(){
        AdamObject aObj = av.GetFocus();
        if(aObj != null){
            Location oLocation =  aObj.GetLocation();
            if(oLocation != null){
                return ((Float)(1/oLocation.getAccuracy())).doubleValue();
            }
        }
        return 0d;
    }
    public Double GetDistanceToFocusObject(){
        AdamObject aObj = av.GetFocus();
        if(aObj != null){

            Location oLocation =  aObj.GetLocation();
            Location mLocation =  ((AdamActivityMain) av.getContext()).GetLocation();
            if((oLocation != null) && (mLocation != null)){
                return ((Float)oLocation.distanceTo(mLocation)).doubleValue()/AdamRadar.DEFAULT_MAX_DIST ;
            }
        }
        return Double.POSITIVE_INFINITY;
    }
    public void Draw(Canvas canvas){
        AdamObject adamObject = this.av.GetFocus();
        if(adamObject == null){
            return;
        }
        canvas.drawRect(
                this.getX(),
                this.getY(),
                MAIN_WINDOW_WIDTH,
                MAIN_WINDOW_HEIGHT,
                bg_paint
        );
        paint.setTextSize(MAIN_WINDOW_HEIGHT/2);
        DrawIcon(
            canvas,
            adamObject.GetIcon(),
            (this.getX() + Math.round(MAIN_WINDOW_WIDTH * .33)),
            (this.getY() + Math.round(MAIN_WINDOW_HEIGHT * .66))
        );
        Enumeration<String> keys = children.keys();
        int intNextY = getY();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamDrawable ad =  this.children.get(key);

            if(ad instanceof AdamStackable){
                ad.setTop(intNextY);
                ad.setLeft(this.getX() + this.MAIN_WINDOW_WIDTH + this.padding);
                ad.Draw(canvas);
                intNextY += ad.getHeight() + this.padding;
            }
        }
    }

}

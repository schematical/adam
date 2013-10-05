package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.schematical.adam.AdamActivityMain;
import com.schematical.adam.AdamObject;
import com.schematical.adam.AdamView;
import com.schematical.adam.drawable.AdamDrawable;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamRadar extends AdamDrawable {
    public static final double DEFAULT_MAX_DIST = 100;


    protected boolean blnFullScreen = false;
    protected int orig_height = 0;
    protected int orig_width = 0;
    public AdamRadar(AdamView nAv) {
        super(nAv);

    }

    public void ToggleFullScreen(){
        if(this.blnFullScreen){
            this.blnFullScreen = false;
            width = orig_width;
            height = orig_height;
        }else{
            this.blnFullScreen = true;
            orig_height = this.height;
            orig_width = this.width;
            width = av.GetCanvas().getHeight()- (2 * padding);
            height = av.GetCanvas().getHeight() - (2 * padding);
        }
    }

    public void Draw(Canvas canvas, double xAngle, double yAngle, double zAngle){




        int middleX = this.getX() + width/2 ;
        int middleY = this.getY() + width/2;

        canvas.drawCircle(
                middleX,
                middleY,
                height/2,
                bg_paint
        );

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(
                middleX,
                middleY,
                10,
                paint
        );

        canvas.drawCircle(
                middleX,
                middleY,
                height/2,
                paint
        );

        paint.setStyle(Paint.Style.FILL);


        canvas.drawText(
                "N",
                Math.round(middleX + Math.cos(xAngle) * width/2),
                Math.round(middleY + Math.sin(xAngle) * height/2),
                paint
        );
        DrawAdamObjects(canvas, width, height);


    }
    public void DrawAdamObjects(Canvas canvas, int nWidth, int nHeight){
        Hashtable<String, AdamObject> aObjects = ((AdamActivityMain)av.getContext()).GetAdamObjects();
        Enumeration<String> keys = aObjects.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamObject ao = (AdamObject) aObjects.get(key);
            ao.Radar().Draw(canvas, this);

        }
    }
    public boolean isFullScreen() {
        return blnFullScreen;
    }
    public void onTouch(View v, MotionEvent event) {
        this.ToggleFullScreen();
    }



}

package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.schematical.adam.drawable.AdamDrawable;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamRadar extends AdamDrawable {
    public static final double DEFAULT_MAX_DIST = 100;
    protected boolean blnFullScreen = false;
    AdamRadar(AdamView nAv) {
        super(nAv);
    }

    public void MakeFullScreen(){
        this.blnFullScreen = true;
    }

    public void Draw(Canvas canvas, double xAngle, double yAngle, double zAngle){
        int nWidth = width;
        int nHeight = height;


        if(this.blnFullScreen){
            nWidth = canvas.getWidth()- 2 * padding;
            nHeight = canvas.getHeight()- 2 * padding;
            /*middleX = (canvas.getWidth()- 20) - nWidth/2 ;
            middleY = (canvas.getHeight() - 20) - nHeight/2;*/
        }
        int middleX = (canvas.getWidth()- padding) - nWidth/2 ;
        int middleY = (canvas.getHeight() - padding) - nHeight/2;

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
                nHeight/2,
                paint
        );

        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(
                "N",
                Math.round(middleX + Math.cos(xAngle) * nWidth/2),
                Math.round(middleY + Math.sin(xAngle) * nHeight/2),
                paint
        );

        DrawAdamObjects(canvas, nWidth, nHeight);

    }
    public void DrawAdamObjects(Canvas canvas, int nWidth, int nHeight){
        Hashtable<String, AdamObject> aObjects = ((AdamActivityMain)av.getContext()).GetAdamObjects();
        Enumeration<String> keys = aObjects.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamObject ao = (AdamObject) aObjects.get(key);
            ao.DrawRadar(canvas, this, nWidth, nHeight);
        }
    }


}

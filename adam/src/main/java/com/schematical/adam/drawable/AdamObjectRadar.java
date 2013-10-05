package com.schematical.adam.drawable;

import android.graphics.Canvas;

import com.schematical.adam.AdamObject;
import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/5/13.
 */
public class AdamObjectRadar extends AdamDrawable {
    AdamObject ao;
    private double radarAngle;
    private double radarDistance;

    public AdamObjectRadar(AdamView nAv, AdamObject nAo) {
        super(nAv);
        this.ao = nAo;
    }

    public void SetRadarXY(double nRadarAngle, double nDistance) {
        this.radarAngle = nRadarAngle;
        this.radarDistance = nDistance;
    }
    public void Draw(Canvas canvas, AdamRadar adamRadar){
        int nWidth = adamRadar.getWidth();
        int nHeight = adamRadar.getHeight();
        paint.setTextSize(20);
        if(
            (ao.GetLat() != 0) &&
            (this.radarDistance < AdamRadar.DEFAULT_MAX_DIST)
        ){
            double radius = this.radarDistance / AdamRadar.DEFAULT_MAX_DIST * (nWidth/2);
            double bigX  = Math.cos(this.radarAngle) *  radius;
            double bigY = Math.sin(this.radarAngle)  * radius;
            canvas.drawCircle(
                (float) bigX + adamRadar.getX() + nWidth / 2,
                (float) bigY + adamRadar.getX() + nHeight / 2,
                5,
                paint
            );
            int degrees = (int)Math.round((this.radarAngle/Math.PI)*180);
            String sDesc = ao.GetAlias() + "(" + Math.round(this.radarDistance) +  " / " + degrees + ")";
            if(adamRadar.isFullScreen()){
                canvas.drawText(
                        sDesc,
                        (float) bigX + adamRadar.getX() +nWidth / 2,
                        (float) bigY + adamRadar.getY() +nHeight / 2,
                        paint
                );
            }


        }
    }
}

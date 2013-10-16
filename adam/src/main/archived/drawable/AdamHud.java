package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.location.Location;

import com.schematical.adam.old.AdamActivityMain;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamHud extends AdamDrawable{


    public AdamHud(AdamView nAv) {
        super(nAv);
    }


    public void Draw(Canvas canvas){
        AdamActivityMain am = (AdamActivityMain) av.getContext();



        Location oLocation = am.GetLocation();
        String msg = "";
        if(oLocation != null){
            /*msg = "Accuracy: " + oLocation.getAccuracy();
            canvas.drawText(
                    msg,
                    100,
                    100,
                    paint
            );*/


            /*msg = "Ping Ct: " + am.pingCt;
            canvas.drawText(
                    msg,
                    100,
                    canvas.getHeight() - 100,
                    paint
            );*/
        }

    }
}

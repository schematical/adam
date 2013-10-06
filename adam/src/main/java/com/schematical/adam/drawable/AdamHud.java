package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;

import com.schematical.adam.AdamActivityMain;
import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamHud extends AdamDrawable{


    public AdamHud(AdamView nAv) {
        super(nAv);
    }


    public void Draw(Canvas canvas){
        AdamActivityMain am = (AdamActivityMain) av.getContext();

        canvas.drawText(
                am.GetStatus(),
                100,
                40,
                paint
        );
        if(av.GetFocus() != null){
            canvas.drawText(
                    av.GetFocus().GetAlias(),
                    100,
                    canvas.getHeight() - 150,
                    paint
            );
        }

        Location oLocation = am.GetLocation();
        String msg = "";
        if(oLocation != null){
            msg = "Accuracy: " + oLocation.getAccuracy();
            canvas.drawText(
                    msg,
                    100,
                    100,
                    paint
            );


            msg = "Ping Ct: " + am.pingCt;
            canvas.drawText(
                    msg,
                    100,
                    canvas.getHeight() - 100,
                    paint
            );
        }

    }
}

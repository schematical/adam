package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;

import com.schematical.adam.AdamActivityMain;
import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamHud {
    private AdamView av;
    private Paint paint;

    public AdamHud(AdamView nAv){
        av = nAv;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(40);

    }
    public void Draw(Canvas canvas){
        AdamActivityMain am = (AdamActivityMain) av.getContext();

        canvas.drawText(
                am.GetStatus(),
                100,
                40,
                paint
        );
       /* String msg = "XYZ: " + Double.toString(av.xAngle) + "- Y:" + Double.toString(av.yAngle) + " - Z:" + Double.toString(av.zAngle);
        canvas.drawText(
                msg,
                100,
                100,
                paint
        );*/


        /*
        msg = "Diff Angle: " + Double.toString(mObjectAngleDiff) + "   - Relitive Angle: " + Double.toString(mObjectRelitiveAngle);
        canvas.drawText(
                msg,
                100,
                200,
                paint
        );*/
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

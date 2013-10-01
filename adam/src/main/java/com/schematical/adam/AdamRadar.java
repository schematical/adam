package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamRadar {
    private final Paint paint;
    private int height = 200;
    private int width = 200;
    protected AdamView av;

    AdamRadar(AdamView nAv){
        av = nAv;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(20);
        paint.setStrokeWidth(4);

    }

    public void Draw(Canvas canvas, double xAngle, double yAngle, double zAngle){
 /*       int middleX = canvas.getHeight() - height/2;
        int middleY = canvas.getWidth() - width/2;*/
        int middleX = (canvas.getWidth()- 20) - width/2 ;
        int middleY = (canvas.getHeight() - 20) - height/2;

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
                100,
                paint
        );




       /* canvas.drawCircle(
                Math.round(middleX + Math.cos(xAngle) * 75),
                Math.round(middleY + Math.sin(xAngle) * 75),
                10,
                paint
        );*/
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(
                "N",
                Math.round(middleX + Math.cos(xAngle) * width/2),
                Math.round(middleY + Math.sin(xAngle) * height/2),
                paint
        );

    }


}

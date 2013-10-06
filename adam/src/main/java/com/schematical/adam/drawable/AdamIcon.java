package com.schematical.adam.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;

import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamIcon extends AdamDrawable{
    protected char icon;

    public AdamIcon(AdamView nAv, char nIcon) {
        super(nAv);
        icon = nIcon;
        this.height = 90;
        this.width = 90;
    }
    public void Draw(Canvas canvas){
        int x = this.getX();
        int y = this.getY();

        DrawBitmap(canvas, av.icon);
        //paint.setTextSize(Math.round(40/cDist));
        Typeface tf= Typeface.createFromAsset(av.getContext().getAssets(),"font/fontawesome-webfont.ttf");
        paint.setTypeface(tf);
        paint.setColor(0xffffffff);
        paint.setTextSize(this.height/2);
        StringBuilder sb = new StringBuilder();

        char c = 0xf09e;
        sb.append(c);
        canvas.drawText(
                sb.toString(),
                this.getX() + this.width/2,
                this.getY() + this.height/2,
                paint
        );
    }
}

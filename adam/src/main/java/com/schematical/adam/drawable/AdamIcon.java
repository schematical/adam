package com.schematical.adam.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;

import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamIcon extends AdamDrawable{
    public static final char building = 0xf0f7;
    public static final char rss = 0xf09e;
    public static final char laptop = 0xf109;
    public static final char home = 0xf015;
    public static final char coffee = 0xf0f4;
    public static final char unlock = 0xf13e;
    public static final char bullseye = 0xf140;
    public static final char eye = 0xf06e;
    public static final char target = 0xf05b;
    public static final char lock = 0xf023;
    public static final char mobile_phone = 0xf10b;
    public static final char mircophone = 0xf130;
    public static final char gamepad = 0xf11b;
    public static final char command_line = 0xf120;
    public static final char question_mark = 0xf128;
    public static final char warning_sign = 0xf071;
    public static final char signle = 0xf012;
    public static final char flag_white = 0xf11d;
    public static final char code = 0xf121;
    public static final char flag = 0xf024;
    public static final char anchor = 0xf13d;
    public static final char not_allowed = 0xf05e;
    public static final char cancel = 0xf057;
    public static final char reload = 0xf079;




    //Services
    public static final char flickr = 0xf16e;
    public static final char instagram = 0xf16d;
    public static final char linkedin = 0xf0e1;
    public static final char twitter = 0xf099;
    protected char icon;

    public AdamIcon(AdamView nAv, char nIcon) {
        super(nAv);
        icon = nIcon;
        this.height = 140;
        this.width = 140;
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

        canvas.drawText(
            new Character(this.icon).toString(),
            this.getX() + Math.round(this.width*.33),
            this.getY() + Math.round(this.height*.66),
            paint
        );
    }
}

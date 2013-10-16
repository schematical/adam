package com.schematical.adam.drawable;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamStackable extends AdamDrawable {
    public static final Double CORNER_NONE = null;
    public static final Double CORNER_TOP_RIGHT = 0d;
    public static final Double CORNER_BOTTOM_RIGHT = Math.PI * .5;
    public static final Double CORNER_BOTTOM_LEFT = Math.PI * 1;
    public static final Double CORNER_TOP_LEFT = Math.PI * 1.5;



    protected Double corner = AdamStackable.CORNER_NONE;
    public AdamStackable(iAdamDrawable nAv) {
        super(nAv);
        this.height = 40;
        this.width = 300;
        this.padding = 25;
        paint.setTextSize(20);
    }
    public void Focus(){
        this.height = 70;
        this.width = 450;
        this.padding = 40;
        paint.setTextSize(35);
        this.InvertColor();
    }

    public Double getCorner() {
        return corner;
    }

    public void setCorner(Double corner) {
        this.corner = corner;
    }

}

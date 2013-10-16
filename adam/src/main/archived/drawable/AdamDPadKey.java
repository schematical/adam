package com.schematical.adam.drawable;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamDPadKey extends AdamIcon{
    public static final Double DIR_UP = 0d;
    public static final Double DIR_RIGHT = Math.PI * .5;
    public static final Double DIR_DOWN = Math.PI;
    public static final Double DIR_LEFT = Math.PI * 1.5;

    protected String actionParameter;
    protected double direction;
    public AdamDPadKey(iAdamDrawable nAv, char charCode) {
        super(nAv,charCode);


    }
    public String getActionParameter() {
        return actionParameter;
    }

    public void setActionParameter(String actionParameter) {
        this.actionParameter = actionParameter;
    }






}

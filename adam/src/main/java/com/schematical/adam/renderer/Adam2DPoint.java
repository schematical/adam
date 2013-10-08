package com.schematical.adam.renderer;

/**
 * Created by user1a on 10/8/13.
 */
public class Adam2DPoint {

    protected Double x;
    protected Double y;
    protected Double scale;



    protected Double metaAngle;
    protected Double metaDistance;
    public Adam2DPoint(Double nX, Double nY, Double nScale){
        x = nX;
        y = nY;
        scale = nScale;
    }
    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getScale() {
        return scale;
    }
    public Double getMetaAngle() {
        return metaAngle;
    }

    public void setMetaAngle(Double metaAngle) {
        this.metaAngle = metaAngle;
    }

    public Double getMetaDistance() {
        return metaDistance;
    }

    public void setMetaDistance(Double metaDistance) {
        this.metaDistance = metaDistance;
    }
}

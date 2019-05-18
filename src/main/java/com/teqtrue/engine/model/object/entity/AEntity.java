package com.teqtrue.engine.model.object.entity;


import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;

import java.io.Serializable;

import com.teqtrue.engine.graphics.Sprite;

public abstract class AEntity implements IEntity, Serializable {

    private Coordinates coordinates;
    private int spriteIndex;
    private double speed;
    private double orientation;
    private static final long serialVersionUID = 1L;

    public AEntity(Coordinates coordinates, int sprite, double speed) {
        this.spriteIndex = sprite;
        this.coordinates = coordinates;
        this.speed = speed;
        this.orientation = 0;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Sprite getSprite() {
        return Config.getSprites()[spriteIndex];
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getOrientation() {
        return getOrientation(false);
    }

    public double getOrientation(boolean radians) {
        if (radians) {
            return Math.toRadians(orientation);
        }
        return orientation;
    }

    public void setOrientation(double orientation) {
        if (orientation < 0) {
            orientation += 360;
        }
        this.orientation = orientation;
    }

}

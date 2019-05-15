package com.teqtrue.engine.model.object.entity;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;

import java.io.Serializable;

import com.teqtrue.engine.graphics.Sprite;

public abstract class AEntity implements IEntity, Serializable {

    private Coordinates coordinates;
    private int spriteIndex;

    public AEntity(Coordinates coordinates, int sprite) {
        this.spriteIndex = sprite;
        this.coordinates = coordinates;
    }

    public abstract void update();

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Sprite getSprite() {
        return Config.getSprites()[spriteIndex];
    }

}

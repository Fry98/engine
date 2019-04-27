package com.teqtrue.engine.model.object.entity;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.graphics.Sprite;

public abstract class AEntity extends GameObject {

    private Coordinates coordinates;

    public AEntity(Coordinates coordinates, Sprite sprite) {
        super(sprite, false);
        this.coordinates = coordinates;
    }

    public abstract void update();

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

}

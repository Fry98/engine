package com.teqtrue.engine.model.object.entity;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.graphics.Sprite;

public abstract class AEntity implements IEntity {

    private Coordinates coordinates;
    private Sprite sprite;

    public AEntity(Coordinates coordinates, Sprite sprite) {
        this.sprite = sprite;
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
        return sprite;
    }

}

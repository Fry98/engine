package com.teqtrue.engine.model.object.entity;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.object.AGameObject;
import com.teqtrue.engine.graphics.Sprite;

public abstract class AEntity extends AGameObject {

    private Coordinates coordinates;

    public AEntity(Coordinates coordinates, Sprite sprite) {
        super(sprite);
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

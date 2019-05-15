package com.teqtrue.engine.model.object.entity;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;

public interface IEntity {
    public void update();
    public Coordinates getCoordinates();
    public void setCoordinates(Coordinates coordinates);
    public Sprite getSprite();
}

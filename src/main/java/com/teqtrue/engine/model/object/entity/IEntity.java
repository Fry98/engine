package com.teqtrue.engine.model.object.entity;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;

public interface IEntity {
    void update();
    Coordinates getCoordinates();
    void setCoordinates(Coordinates coordinates);
    Sprite getSprite();
    double getSpeed();
    void setSpeed(double speed);
    double getOrientation();
    void setOrientation(double orientation);
}

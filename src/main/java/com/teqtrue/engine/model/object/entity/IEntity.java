package com.teqtrue.engine.model.object.entity;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;

import java.util.List;

public interface IEntity {
    void update(List<IEntity> entities);
    Coordinates getCoordinates();
    void setCoordinates(Coordinates coordinates);
    Sprite getSprite();
    double getSpeed();
    void setSpeed(double speed);
    double getOrientation();
    void setOrientation(double orientation);
}

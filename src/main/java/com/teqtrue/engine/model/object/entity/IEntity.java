package com.teqtrue.engine.model.object.entity;

import java.util.ArrayList;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.object.Projectile;

public interface IEntity {
    Runnable update(ArrayList<Projectile> projectiles);
    Coordinates getCoordinates();
    void setCoordinates(Coordinates coordinates);
    Sprite getSprite();
    double getSpeed();
    void setSpeed(double speed);
    double getOrientation();
    void setOrientation(double orientation);
}

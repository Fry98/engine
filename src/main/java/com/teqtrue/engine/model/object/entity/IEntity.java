package com.teqtrue.engine.model.object.entity;

import java.util.ArrayList;
import java.util.UUID;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.screen.GameScreen;

public interface IEntity {
    Runnable update(ArrayList<Projectile> projectiles, GameScreen parent);
    Coordinates getCoordinates();
    void setCoordinates(Coordinates coordinates);
    Sprite getSprite();
    double getSpeed();
    void setSpeed(double speed);
    double getOrientation();
    void setOrientation(double orientation);
    UUID getUuid();
}

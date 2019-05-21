package com.teqtrue.engine.model.object.entity;


import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.Coordinates;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.teqtrue.engine.graphics.Sprite;

public abstract class AEntity implements IEntity, Serializable {

    private Coordinates coordinates;
    private int spriteIndex;
    private double speed;
    private double orientation;
    private UUID uuid;
    private static final long serialVersionUID = 1L;

    public AEntity(Coordinates coordinates, int sprite, double speed) {
        this.spriteIndex = sprite;
        this.coordinates = coordinates;
        this.speed = speed;
        this.orientation = 0;
        this.uuid = UUID.randomUUID();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Sprite getSprite() {
        return GlobalStore.getSprites()[spriteIndex];
    }

    protected void setSprite(int spriteIndex) {
        this.spriteIndex = spriteIndex;
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

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AEntity aEntity = (AEntity) o;
        return Objects.equals(uuid, aEntity.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}

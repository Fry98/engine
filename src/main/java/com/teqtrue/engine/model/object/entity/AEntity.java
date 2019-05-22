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
    private int maxHealth;
    private int health;
    private UUID uuid;
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new entity.
     * @param coordinates initial position
     * @param sprite sprite index
     * @param speed initial and maximal speed
     * @param maxHealth initial and maximal health
     */
    public AEntity(Coordinates coordinates, int sprite, double speed, int maxHealth) {
        this.spriteIndex = sprite;
        this.coordinates = coordinates;
        this.speed = speed;
        this.orientation = 0;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
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

    public void setSprite(int spriteIndex) {
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void adjustHealth(int health) {
        this.health += health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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

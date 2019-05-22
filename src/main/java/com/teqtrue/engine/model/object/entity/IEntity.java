package com.teqtrue.engine.model.object.entity;

import java.util.UUID;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.screen.GameScreen;

public interface IEntity {
    /**
     * Update method is called by the engine on each frame.
     * All entities should use it to check for interactions with the world
     * and to update their own state.
     * @param parent parent screen representing current state of the game
     * @return a {@code Runnable} object from which a new thread will be created
     */
    Runnable update(GameScreen parent);

    /**
     * Returns current position of the entity.
     */
    Coordinates getCoordinates();

    /**
     * Sets new position of the entity.
     * @param coordinates new position to set
     */
    void setCoordinates(Coordinates coordinates);

    /**
     * Returns {@code Sprite} associated with this entity.
     */
    Sprite getSprite();

    /**
     * Sets new sprite for this entity by index.
     * @param spriteIndex index of sprite to use
     */
    void setSprite(int spriteIndex);

    /**
     * Returns speed of the entity;
     */
    double getSpeed();

    /**
     * Sets speed of the entity.
     * @param speed value to set
     */
    void setSpeed(double speed);

    /**
     * Returns current orientation of the entity in degrees.
     */
    double getOrientation();

    /**
     * Returns current orientation of the entity.
     * @param degrees if {@code true}, the value is returned in degrees, otherwise in radians
     */
    double getOrientation(boolean degrees);

    /**
     * Sets current orientation of the entity. Value {@code 0} is to the right.
     * @param orientation new value in degrees
     */
    void setOrientation(double orientation);

    /**
     * Returns current health of the entity.
     */
    int getHealth();

    /**
     * Sets health of the entity.
     * @param h value to set
     */
    void setHealth(int h);

    /**
     * Modifies health of the entity. No boundary checks are applied.
     * @param h this number will be added to current health of the entity
     */
    void adjustHealth(int h);

    /**
     * Returns maximal health of the entity.
     */
    int getMaxHealth();

    /**
     * Sets maximal health of the entity.
     * @param maxHealth value to set
     */
    void setMaxHealth(int maxHealth);

    /**
     * Returns unique ID of this entity.
     */
    UUID getUuid();
}

package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.object.entity.AEntity;

public class Player extends AEntity {

    private boolean isMoving;

    public Player(Coordinates coordinates) {
        super(coordinates, 7, 3);
        this.isMoving = false;
    }

    @Override
    public void update() {
        Coordinates oldCoords = getCoordinates();
        if (isMoving) {
            double x = oldCoords.getX() - Math.sin(Math.toRadians(getOrientation())) * getSpeed() * 0.02;
            double y = oldCoords.getY() + Math.cos(Math.toRadians(getOrientation())) * getSpeed() * 0.02;
            setCoordinates(new Coordinates(x, y));
        }
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean set) {
        this.isMoving = set;
    }

}

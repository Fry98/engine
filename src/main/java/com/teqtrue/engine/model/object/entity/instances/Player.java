package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.object.entity.AEntity;

public class Player extends AEntity {

    public Player(Coordinates coordinates) {
        super(coordinates, 7, 3);
    }

    @Override
    public void update() {
        boolean isMoving = true;
        Coordinates oldCoords = getCoordinates();

        if (isMoving) {
            double x = oldCoords.getX() - Math.sin(Math.toRadians(getOrientation())) * getSpeed() * 0.02;
            double y = oldCoords.getY() + Math.cos(Math.toRadians(getOrientation())) * getSpeed() * 0.02;
            setCoordinates(new Coordinates(x, y));
        }

    }

}

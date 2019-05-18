package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.entity.AEntity;

public class Player extends AEntity {

    public Player(Coordinates coordinates) {
        super(coordinates, 7, 8);
    }

    @Override
    public void update() {
        Coordinates pos = getCoordinates();
        Coordinates movementVector = KeyMap.getMovementVector();
        pos.alterX(movementVector.getX() * getSpeed() * 0.02);
        pos.alterY(movementVector.getY() * getSpeed() * 0.02);
    }

}

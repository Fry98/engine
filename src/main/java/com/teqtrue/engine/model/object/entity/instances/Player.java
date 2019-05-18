package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.entity.AEntity;
import javafx.scene.input.KeyCode;

public class Player extends AEntity {

    public Player(Coordinates coordinates) {
        super(coordinates, 7, 8);
    }

    @Override
    public void update() {
        // WASD+Shift movement
        Coordinates pos = getCoordinates();
        Coordinates movementVector = KeyMap.getMovementVector();
        double speedMult = 1.0;
        if (KeyMap.isPressed(KeyCode.SHIFT)) {
            speedMult = 2.0;
        }
        pos.alterX(movementVector.getX() * getSpeed() * speedMult * 0.02);
        pos.alterY(movementVector.getY() * getSpeed() * speedMult * 0.02);

        // rotate towards the mouse
        Coordinates mouse = KeyMap.getMouse();
        Coordinates screenSize = Config.getScreenSize();
        double dx = mouse.getX() - screenSize.getX() / 2;
        double dy = mouse.getY() - screenSize.getY() / 2;
        setOrientation(Math.toDegrees(Math.atan2(dy, dx)) - 90); // for atan2 0deg is to the right, for us it's to the down
    }

}

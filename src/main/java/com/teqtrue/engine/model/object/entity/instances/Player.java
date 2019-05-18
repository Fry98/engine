package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.AEntity;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class Player extends AEntity {

    private int countdown = 0;
    private static final long serialVersionUID = 1L;

    public Player(Coordinates coordinates) {
        super(coordinates, 7, 8);
    }

    @Override
    public Runnable update() {
        return new Runnable() {
            @Override
            public void run() {
                // WASD+Shift movement
                Coordinates pos = getCoordinates();
                Coordinates movementVector = KeyMap.getMovementVector();
                GameMap gameMap = GlobalStore.getMap();
                double speedMult = 1.0;
                if (KeyMap.isPressed(KeyCode.SHIFT)) {
                    speedMult = 2.0;
                }
                double newX = pos.getX() + movementVector.getX() * getSpeed() * speedMult * 0.02;
                double newY = pos.getY() + movementVector.getY() * getSpeed() * speedMult * 0.02;

                // VERTICAL COLLISIONS
                if (KeyMap.isPressed(KeyCode.W)) {
                    Coordinates topLeftTile = new Coordinates(Math.floor(pos.getX()), Math.floor(newY));
                    if (gameMap.hasCollision(topLeftTile)) {
                        newY = Math.ceil(newY);
                    } else {
                        Coordinates topRightTile = new Coordinates(Math.floor(pos.getX() + 1), Math.floor(newY));
                        if (gameMap.hasCollision(topRightTile)) {
                            newY = Math.ceil(newY);
                        }
                    }
                } else if (KeyMap.isPressed(KeyCode.S)) {
                    Coordinates bottomLeftTile = new Coordinates(Math.floor(pos.getX()), Math.floor(newY + 1));
                    if (gameMap.hasCollision(bottomLeftTile)) {
                        newY = Math.floor(newY) - 0.1;
                    } else {
                        Coordinates bottomRightTile = new Coordinates(Math.floor(pos.getX() + 1), Math.floor(newY + 1));
                        if (gameMap.hasCollision(bottomRightTile)) {
                            newY = Math.floor(newY) - 0.1;
                        }
                    }
                }

                // HORIZONTAL COLLISIONS
                if (KeyMap.isPressed(KeyCode.A)) {
                    Coordinates topLeftTile = new Coordinates(Math.floor(newX), Math.floor(pos.getY()));
                    if (gameMap.hasCollision(topLeftTile)) {
                        newX = Math.ceil(newX);
                    } else {
                        Coordinates bottomLeftTile = new Coordinates(Math.floor(newX), Math.floor(pos.getY() + 1));
                        if (gameMap.hasCollision(bottomLeftTile)) {
                            newX = Math.ceil(newX);
                        }
                    }
                } else if (KeyMap.isPressed(KeyCode.D)) {
                    Coordinates topRightTile = new Coordinates(Math.floor(newX + 1), Math.floor(pos.getY()));
                    if (gameMap.hasCollision(topRightTile)) {
                        newX = Math.floor(newX) - 0.1;
                    } else {
                        Coordinates bottomRightTile = new Coordinates(Math.floor(newX + 1), Math.floor(pos.getY() + 1));
                        if (gameMap.hasCollision(bottomRightTile)) {
                            newX = Math.floor(newX) - 0.1;
                        }
                    }
                }

                pos.setX(newX);
                pos.setY(newY);

                if (countdown > 0) {
                    countdown--;
                }

                if (KeyMap.isMousePressed(MouseButton.PRIMARY) && countdown == 0) {
                    double orientation = getOrientation();

                    gameMap.addProjectile(new Projectile(
                        GlobalStore.getSprites()[10],
                        new Coordinates(newX + 0.5, newY + 0.5),
                        new Coordinates(Math.cos(Math.toRadians(orientation)), Math.sin(Math.toRadians(orientation)))
                    ));
                    countdown = 4;
                }

                // rotate towards the mouse
                Coordinates mouse = KeyMap.getMouse();
                Coordinates screenSize = GlobalStore.getScreenSize();
                double dx = mouse.getX() - screenSize.getX() / 2;
                double dy = mouse.getY() - screenSize.getY() / 2;
                setOrientation(Math.toDegrees(Math.atan2(dy, dx)));
            }
        };
    }
}

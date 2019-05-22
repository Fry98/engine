package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.GlobalStore;

import java.util.ArrayList;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.AEntity;
import com.teqtrue.engine.screen.GameScreen;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class Player extends AEntity {

    private int cooldown = 0;
    private static final long serialVersionUID = 1L;
    private int damageCountdown = 0;

    /**
     * Creates new player.
     * @param coordinates initial position
     */
    public Player(Coordinates coordinates) {
        super(coordinates, 7, 8, 100);
    }

    @Override
    public Runnable update(ArrayList<Projectile> projectiles, GameScreen parent) {
        return () -> {
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

            // projectile collisions
            for (Projectile projectile : projectiles) {
                Coordinates coords = projectile.getPosition();
                if (coords.getX() > pos.getX() && coords.getX() < pos.getX() + 1 && coords.getY() > pos.getY() && coords.getY() < pos.getY() + 1) {
                    adjustHealth(-5);
                    gameMap.removeProjectile(projectile);
                    setSprite(14);
                    damageCountdown = 10;
                }
            }

            if (getHealth() <= 0) {
                parent.kill(false);
            }

            if (damageCountdown > 0) {
                damageCountdown--;
                if (damageCountdown == 0) {
                    setSprite(7);
                }
            }

            // vertical collisions
            if (movementVector.getY() < 0) {
                if (gameMap.hasCollision(pos.getX(), newY) || gameMap.hasCollision(pos.getX() + 1, newY)) {
                    newY = Math.ceil(newY);
                }
            } else if (movementVector.getY() > 0) {
                if (gameMap.hasCollision(pos.getX(), newY + 1) || gameMap.hasCollision(pos.getX() + 1, newY + 1)) {
                    newY = Math.floor(newY) - 0.01;
                }
            }
            // horizontal collisions
            if (movementVector.getX() < 0) {
                if (gameMap.hasCollision(newX, pos.getY()) || gameMap.hasCollision(newX, pos.getY() + 1)) {
                    newX = Math.ceil(newX);
                }
            } else if (movementVector.getX() > 0) {
                if (gameMap.hasCollision(newX + 1, pos.getY()) || gameMap.hasCollision(newX + 1, pos.getY() + 1)) {
                    newX = Math.floor(newX) - 0.01;
                }
            }

            pos.setX(newX);
            pos.setY(newY);

            if (cooldown > 0) {
                cooldown--;
            }

            if (KeyMap.isMousePressed(MouseButton.PRIMARY) && cooldown == 0) {
                double orientation = getOrientation();

                gameMap.addProjectile(new Projectile(
                    GlobalStore.getSprites()[10],
                    new Coordinates(newX + 0.5, newY + 0.5),
                    new Coordinates(Math.cos(Math.toRadians(orientation)), Math.sin(Math.toRadians(orientation)))
                ));
                cooldown = 5;
            }

            // rotate towards the mouse
            Coordinates mouse = KeyMap.getMouse();
            Coordinates screenSize = GlobalStore.getScreenSize();
            double dx = mouse.getX() - screenSize.getX() / 2;
            double dy = mouse.getY() - screenSize.getY() / 2;
            setOrientation(Math.toDegrees(Math.atan2(dy, dx)));
        };
    }
}

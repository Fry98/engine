package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.AEntity;
import com.teqtrue.engine.model.object.entity.IEntity;

import java.util.List;

public class BasicEnemy extends AEntity {

    private int cooldown = 0;
    private static final long serialVersionUID = 1L;

    public BasicEnemy(Coordinates coordinates) {
        super(coordinates, 8, 5);
    }

    @Override
    public Runnable update() {
        return new Runnable(){
            @Override
            public void run() {
                Coordinates pos = getCoordinates();
                GameMap gameMap = GlobalStore.getMap();
                double newX = pos.getX();
                double newY = pos.getY();

                if (cooldown > 0) {
                    cooldown--;
                }

                // find the player
                IEntity player = findPlayer(gameMap.getEntities());
                if (player != null) {
                    Coordinates playerPos = player.getCoordinates();
                    double dx = playerPos.getX() - pos.getX();
                    double dy = playerPos.getY() - pos.getY();
        
                    // rotate towards the player
                    setOrientation(Math.toDegrees(Math.atan2(dy, dx)));
        
                    // run towards the player
                    if (dx*dx + dy*dy > 4) {
                        newX += Math.cos(getOrientation(true)) * getSpeed() * 0.02;
                        newY += Math.sin(getOrientation(true)) * getSpeed() * 0.02;
                    }

                    // shoot at him
                    if (cooldown == 0) {
                        double orientation = getOrientation();

                        gameMap.addProjectile(new Projectile(
                                GlobalStore.getSprites()[9],
                                new Coordinates(newX + 0.5, newY + 0.5),
                                new Coordinates(Math.cos(Math.toRadians(orientation)), Math.sin(Math.toRadians(orientation)))
                        ));
                        cooldown = 5;
                    }
                }

                // vertical collisions
                if (getOrientation() > 180 && getOrientation() < 360) {
                    if (gameMap.hasCollision(pos.getX(), newY) || gameMap.hasCollision(pos.getX() + 1, newY)) {
                        newY = Math.ceil(newY);
                    }
                } else if (getOrientation() > 0 && getOrientation() < 180) {
                    if (gameMap.hasCollision(pos.getX(), newY + 1) || gameMap.hasCollision(pos.getX() + 1, newY + 1)) {
                        newY = Math.floor(newY) - 0.01;
                    }
                }
                // horizontal collisions
                if (getOrientation() > 90 && getOrientation() < 270) {
                    if (gameMap.hasCollision(newX, pos.getY()) || gameMap.hasCollision(newX, pos.getY() + 1)) {
                        newX = Math.ceil(newX);
                    }
                } else if (getOrientation() > 270 || getOrientation() < 90) {
                    if (gameMap.hasCollision(newX + 1, pos.getY()) || gameMap.hasCollision(newX + 1, pos.getY() + 1)) {
                        newX = Math.floor(newX) - 0.01;
                    }
                }
                pos.setX(newX);
                pos.setY(newY);
            }
        };
    }

    private IEntity findPlayer(List<IEntity> entities) {
        return entities.stream().filter(Player.class::isInstance).findAny().orElse(null);
    }

}

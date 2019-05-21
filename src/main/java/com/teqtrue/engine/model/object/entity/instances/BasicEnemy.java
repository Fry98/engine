package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.AEntity;
import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class BasicEnemy extends AEntity {

    private int cooldown = 0;
    private static final long serialVersionUID = 1L;
    private int damageCountdown = 0;
    private boolean playerVisible = false;

    public BasicEnemy(Coordinates coordinates) {
        super(coordinates, 8, 5, 70);
    }

    @Override
    public Runnable update(ArrayList<Projectile> projectiles, GameScreen parent) {
        BasicEnemy me = this;
        return () -> {
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
                double dist = Math.sqrt(dx*dx + dy*dy);

                if (!playerVisible) {
                    playerVisible = true;
                    double x = pos.getX();
                    double y = pos.getY();
                    for (int i = 0; i <= dist; i++) {
                        if (gameMap.hasCollision((int) x, (int) y)) {
                            playerVisible = false;
                            break;
                        }
                        x += dx / dist;
                        y += dy / dist;
                    }
                }

                if (playerVisible) {
                    // rotate towards the player
                    if (cooldown == 0) {
                        setOrientation(Math.toDegrees(Math.atan2(dy, dx)) + Math.random() * 40 - 20);
                    }

                    // run towards the player
                    if (dist > 2) {
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
                        cooldown = 10;
                    }
                }
            }

            // projectile collisions
            for (Projectile projectile : projectiles) {
                Coordinates coords = projectile.getPosition();
                if (coords.getX() > pos.getX() && coords.getX() < pos.getX() + 1 && coords.getY() > pos.getY() && coords.getY() < pos.getY() + 1) {
                    adjustHealth(-7);
                    gameMap.removeProjectile(projectile);
                    setSprite(13);
                    damageCountdown = 10;
                }
            }

            if (getHealth() <= 0) {
                gameMap.removeEntity(me);
            }

            if (damageCountdown > 0) {
                damageCountdown--;
                if (damageCountdown == 0) {
                    setSprite(8);
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
        };
    }

    private IEntity findPlayer(List<IEntity> entities) {
        return entities.stream().filter(Player.class::isInstance).findAny().orElse(null);
    }

}

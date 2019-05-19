package com.teqtrue.engine.model.object;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GlobalStore;

public class Projectile {
    private Sprite sprite;
    private Coordinates pos;
    private Coordinates vec;

    public Projectile(Sprite sprite, Coordinates position, Coordinates movementVector) {
        this.sprite = sprite;
        pos = new Coordinates(
            position.getX() + movementVector.getX() * 0.75,
            position.getY() + movementVector.getY() * 0.75
        );
        vec = movementVector;
    }

    public Runnable update() {
        Projectile me = this;
        return () -> {
            double newX = pos.getX() + vec.getX() * 0.6;
            double newY = pos.getY() + vec.getY() * 0.6;

            if (GlobalStore.getMap().hasCollision(new Coordinates(Math.floor(newX), Math.floor(newY)))) {
                GlobalStore.getMap().removeProjectile(me);
                return;
            }

            pos = new Coordinates(newX, newY);
        };
    }

    public void setPosition(Coordinates position) {
        pos = position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Coordinates getPosition() {
        return pos;
    }

    public Coordinates getMovementVector() {
        return vec;
    }

    public boolean equals(Projectile p2) {
        return p2.pos.equals(pos) && p2.vec.equals(vec);
    }
}

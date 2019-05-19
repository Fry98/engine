package com.teqtrue.engine.model.object;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GlobalStore;

public class Projectile {
    private Sprite sprite;
    private Coordinates pos;
    private Coordinates vec;
    private boolean dead;

    public Projectile(Sprite sprite, Coordinates position, Coordinates movementVector) {
        this.sprite = sprite;
        pos = new Coordinates(
            position.getX() + movementVector.getX() * 0.75,
            position.getY() + movementVector.getY() * 0.75
        );
        vec = movementVector;
    }

    public Runnable update() {
        return new Runnable(){        
            @Override
            public void run() {
                double newX = pos.getX() + vec.getX() * 0.6;
                double newY = pos.getY() + vec.getY() * 0.6;

                if (GlobalStore.getMap().hasCollision(new Coordinates(Math.floor(newX), Math.floor(newY)))) {
                    dead = true;
                    return;
                }

                pos = new Coordinates(newX, newY);
            }
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

    public boolean isDead() {
        return dead;
    }

    public void kill() {
        dead = true;
    }
}

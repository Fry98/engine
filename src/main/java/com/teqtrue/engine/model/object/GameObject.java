package com.teqtrue.engine.model.object;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Config;

import javafx.scene.canvas.GraphicsContext;

public class GameObject {

    private Sprite sprite;
    private boolean collider;

    public GameObject(Sprite sprite, boolean collider) {
        this.sprite = sprite;
        this.collider = collider;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void drawObject(GraphicsContext gc, double x, double y) {
        sprite.drawSprite(gc, x, y);
    }
}

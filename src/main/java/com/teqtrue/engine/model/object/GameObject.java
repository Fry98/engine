package com.teqtrue.engine.model.object;

import com.teqtrue.engine.graphics.Sprite;

import javafx.scene.canvas.GraphicsContext;

public class GameObject {

    private Sprite sprite;
    private boolean collider;

    /**
     * Creates new game object.
     * @param sprite graphcis of this object
     * @param collider if {@code true}, this object cannot be walked through
     */
    public GameObject(Sprite sprite, boolean collider) {
        this.sprite = sprite;
        this.collider = collider;
    }

    /**
     * Returns sprite of this object.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Sets sprite for this object.
     * @param sprite new sprite to set
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Draws this object on specified position on the canvas.
     * @param gc the canvas
     * @param x first coordinate
     * @param y second coordinate
     */
    public void drawObject(GraphicsContext gc, double x, double y) {
        sprite.drawSprite(gc, x, y);
    }

    /**
     * Returns {@code true} if this object cannot be walked through, {@code false} otherwise.
     */
    public boolean hasCollision() {
        return collider;
    }
}

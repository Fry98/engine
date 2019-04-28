package com.teqtrue.engine.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {

    private Image image;
    private int layer;
    private double sx, sy, sw, sh;

    /**
     * Create a sprite from an image.
     * @param img the image to be drawn.
     */
    public Sprite(Image img) {
        this(img, 0);
    }

    /**
     * Create a sprite from an image that should be drawn in specified layer.
     * Sprites with higher layer are drawn above those with smaller layer.
     * @param img the image to be drawn.
     * @param layer the layer in which the sprite should be drawn.
     */
    public Sprite(Image img, int layer) {
        this(img, 0, 0, img.getWidth(), img.getHeight(), layer);
    }

    /**
     * Create a sprite from a tiled image that should be drawn in specified layer.
     * Sprites with higher layer are drawn above those with smaller layer.
     * @param img the source tiled image.
     * @param sx the source rectangle's X coordinate position.
     * @param sy the source rectangle's Y coordinate position.
     * @param sw the source rectangle's width.
     * @param sh the source rectangle's height.
     * @param layer the layer in which the sprite should be drawn.
     */
    public Sprite(Image img, double sx, double sy, double sw, double sh, int layer) {
        this.image = img;
        this.sx = sx;
        this.sy = sy;
        this.sw = sw;
        this.sh = sh;
        this.layer = layer;
    }

    public void drawSprite(GraphicsContext gc, double x, double y) {
        gc.drawImage(image, sx, sy, sw, sh, x, y, sw, sh);
    }
}

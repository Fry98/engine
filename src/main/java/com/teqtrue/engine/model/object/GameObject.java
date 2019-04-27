package com.teqtrue.engine.model.object;

import com.teqtrue.engine.graphics.Sprite;

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

}

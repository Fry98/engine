package com.teqtrue.engine.model.object;

import com.teqtrue.engine.graphics.Sprite;

public abstract class AGameObject {

    private Sprite sprite;

    public AGameObject(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

}

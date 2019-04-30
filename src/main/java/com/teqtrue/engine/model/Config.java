package com.teqtrue.engine.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.utils.ImageScaler;

import javafx.scene.image.Image;

public class Config {
    private static Image spritesheet;

    public static void init() {
        try {
            spritesheet = ImageScaler.scale("src/main/assets/sprites.png", 16, 48);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getTileSize() {
        return 48;
    }

    public static Coordinates getScreenSize() {
        return new Coordinates(960, 720);
    }

    public static Image getSpritesheet() {
        return spritesheet;
    }
}

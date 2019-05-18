package com.teqtrue.engine.model;

import java.io.FileNotFoundException;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.utils.ImageScaler;

import javafx.scene.image.Image;

public class GlobalStore {
    private static Image spritesheet;
    private static Sprite[] sprites;
    private static GameObject[] registeredObjects;
    private static GameMap map = null;

    private static final int TILE_SIZE = 48;
    private static final Coordinates SCREEN_SIZE = new Coordinates(960,  720);

    public static void init() {
        try {
            // LOAD SPRITESHEET
            spritesheet = ImageScaler.scale("src/main/assets/sprites.png", 16, getTileSize());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // LOAD ALL SPRITES FROM SPRITESHEET
        int tileCount = (int) Math.floor(spritesheet.getWidth() / getTileSize());
        sprites = new Sprite[tileCount];
        for (int i = 0; i < tileCount; i++) {
            sprites[i] = new Sprite(spritesheet, getTileSize() * i, 0, getTileSize(), getTileSize());
        }

        // ASSIGN COLLISIONS TO SPRITES
        Boolean[] collisions = new Boolean[]{
            false,
            true,
            true,
            true,
            true,
            true,
            true
        };

        // REGISTER GAMEOBJECTS
        registeredObjects = new GameObject[collisions.length];
        for (int i = 0; i < collisions.length; i++) {
            registeredObjects[i] = new GameObject(sprites[i], collisions[i]);
        }
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public static final Coordinates getScreenSize() {
        return SCREEN_SIZE;
    }

    public static Image getSpritesheet() {
        return spritesheet;
    }

    public static Sprite[] getSprites() {
        return sprites;
    }

    public static GameObject[] getRegisteredObjects() {
        return registeredObjects;
    }

    public static void setMap(GameMap gameMap) {
        map = gameMap;
    }

    public static GameMap getMap() {
        return map;
    }
}

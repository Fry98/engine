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

    /**
     * Initializes global properties of the engine, eg. loads sprite sheet and sets game object properties.
     * Call only once per run.
     */
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

    /**
     * Returns width of one tile in pixels.
     */
    public static int getTileSize() {
        return TILE_SIZE;
    }

    /**
     * Returns a {@code Coordinates} pair with the size of the screen in pixels.
     */
    public static final Coordinates getScreenSize() {
        return SCREEN_SIZE;
    }

    /**
     * Returns an image spritesheet.
     */
    public static Image getSpritesheet() {
        return spritesheet;
    }

    /**
     * Returns an array of all available sprites.
     */
    public static Sprite[] getSprites() {
        return sprites;
    }

    /**
     * Returns an array of all game objects known to the engine.
     */
    public static GameObject[] getRegisteredObjects() {
        return registeredObjects;
    }

    /**
     * Sets a current {@code GameMap}.
     * @param gameMap current game
     */
    public static void setMap(GameMap gameMap) {
        map = gameMap;
    }

    /**
     * Returns a {@code GameMap} of the current game.
     */
    public static GameMap getMap() {
        return map;
    }
}

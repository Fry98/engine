package com.teqtrue.engine.screen;

import java.util.ArrayList;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.GameObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EditorScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private Coordinates camera = new Coordinates(0, 0);
    private GameMap gameMap = new GameMap();

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                gameMap.set(x, y, 0);
            }
        }
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loop() throws InterruptedException {
        while (true) {
            long tickStart = System.currentTimeMillis();
            update();
            draw();
            long tickDuration = System.currentTimeMillis() - tickStart;
            long timeout = 20 - tickDuration;
            if (timeout > 0) {
                Thread.sleep(20 - tickDuration);
            }
        }
    }

    private void update() {
        int speed = 10;
        if (KeyMap.isPressed("Shift")) {
            speed = 20;
        }
        if (KeyMap.isPressed("W")) {
            if (camera.getY() - speed >= 0) {
                camera.alterY(-speed);
            } else {
                camera.setY(0);
            }
        } 
        if (KeyMap.isPressed("S")) {
            camera.alterY(speed);
        }
        if (KeyMap.isPressed("A")) {
            if (camera.getX() - speed >= 0) {
                camera.alterX(-speed);
            } else {
                camera.setX(0);
            }
        }
        if (KeyMap.isPressed("D")) {
            camera.alterX(speed);
        }
    }

    private void draw() {
        // CLEAR SCREEN
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, Config.getScreenSize().getX(), Config.getScreenSize().getY());

        // DRAW OBJECTS IN GAMEMAP
        int leftX = (int) Math.floor(camera.getX() / Config.getTileSize());
        int upY = (int) Math.floor(camera.getY() / Config.getTileSize());
        int rightX = (int) Math.floor((camera.getX() + Config.getScreenSize().getX()) / Config.getTileSize());
        int downY = (int) Math.floor((camera.getY() + Config.getScreenSize().getY()) / Config.getTileSize());

        for (int x = leftX; x <= rightX; x++) {
            for (int y = upY; y <= downY; y++) {
                ArrayList<GameObject> objArr = gameMap.get(x, y);
                if (objArr != null) {
                    double cornerX = ((x * Config.getTileSize()) - camera.getX());
                    double cornerY = ((y * Config.getTileSize()) - camera.getY());
                    for (GameObject obj : objArr) {
                        obj.drawObject(gc, cornerX, cornerY);
                    }
                }
            }
        }

        // SELECTED TILE CALCULATION
        int tileX = (int) Math.floor((camera.getX() + KeyMap.getMouse().getX()) / Config.getTileSize());
        int tileY = (int) Math.floor((camera.getY() + KeyMap.getMouse().getY()) / Config.getTileSize());
        int cornerX = (int) ((tileX * Config.getTileSize()) - camera.getX());
        int cornerY = (int) ((tileY * Config.getTileSize()) - camera.getY());

        // DRAW SELECTED OBJECT
        // TODO

        // DRAW HIGHLIGHTER
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(cornerX + 1, cornerY + 1, 47, 47);
    }
    
    @Override
    public IApplicationScreen getNextScreen() {
        return new ExitScreen();
    }

}

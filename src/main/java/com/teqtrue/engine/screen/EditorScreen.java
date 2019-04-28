package com.teqtrue.engine.screen;

import java.util.ArrayList;

import com.teqtrue.engine.graphics.Sprite;
import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.utils.ImageScaler;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EditorScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private Coordinates camera = new Coordinates(0, 0);
    private GameMap gameMap = new GameMap();

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;
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
        if (KeyMap.isPressed(38) && camera.getY() > 0) {
            camera.alterY(-10);
        } 
        if (KeyMap.isPressed(40)) {
            camera.alterY(10);
        }
        if (KeyMap.isPressed(37) && camera.getX() > 0) {
            camera.alterX(-10);
        }
        if (KeyMap.isPressed(39)) {
            camera.alterX(10);
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

        // DRAW HIGHLIGHTER
        int tileX = (int) Math.floor((camera.getX() + KeyMap.getMouse().getX()) / Config.getTileSize());
        int tileY = (int) Math.floor((camera.getY() + KeyMap.getMouse().getY()) / Config.getTileSize());
        int cornerX = (int) ((tileX * Config.getTileSize()) - camera.getX());
        int cornerY = (int) ((tileY * Config.getTileSize()) - camera.getY());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(cornerX + 1, cornerY + 1, 47, 47);
    }
    
    @Override
    public IApplicationScreen getNextScreen() {
        return new ExitScreen();
    }

}

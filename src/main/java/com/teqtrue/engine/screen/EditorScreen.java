package com.teqtrue.engine.screen;

import java.util.ArrayList;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.GameObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public class EditorScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private Coordinates camera = new Coordinates(0, 0);
    private GameMap gameMap = new GameMap();
    private int selectedObj = 0;

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;

        // SCROLL EVENT HANDLER
        gc.getCanvas().getScene().setOnScroll(e -> {
            if (e.getDeltaY() > 0 && selectedObj > 0) {
                selectedObj--;
            } else if (e.getDeltaY() < 0 && selectedObj < (Config.getRegisteredObjects().length - 2)) {
                selectedObj++;
            }
        });

        // MOUSE CLICK HANDLER
        gc.getCanvas().getScene().setOnMouseClicked(e -> {
            int tileX = (int) Math.floor((camera.getX() + KeyMap.getMouse().getX()) / Config.getTileSize());
            int tileY = (int) Math.floor((camera.getY() + KeyMap.getMouse().getY()) / Config.getTileSize());
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                gameMap.push(tileX, tileY, selectedObj);
                return;
            }
            gameMap.pop(tileX, tileY);
        });

        // START LOOP
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
        if (KeyMap.isPressed(KeyCode.SHIFT)) {
            speed = 20;
        }
        if (KeyMap.isPressed(KeyCode.W)) {
            if (camera.getY() - speed >= 0) {
                camera.alterY(-speed);
            } else {
                camera.setY(0);
            }
        } 
        if (KeyMap.isPressed(KeyCode.S)) {
            camera.alterY(speed);
        }
        if (KeyMap.isPressed(KeyCode.A)) {
            if (camera.getX() - speed >= 0) {
                camera.alterX(-speed);
            } else {
                camera.setX(0);
            }
        }
        if (KeyMap.isPressed(KeyCode.D)) {
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
        gc.setGlobalAlpha(0.7);
        Config.getRegisteredObjects()[selectedObj].drawObject(gc, cornerX, cornerY);
        gc.setGlobalAlpha(1);

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

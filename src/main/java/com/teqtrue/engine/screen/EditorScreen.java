package com.teqtrue.engine.screen;

import java.io.FileNotFoundException;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.utils.ImageScaler;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EditorScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private Coordinates camera = new Coordinates(0, 0);
    private Image sprites;

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;
        try {
            this.sprites = ImageScaler.scale("src/main/assets/env_day.png", 16, Config.getTileSize());
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loop() throws FileNotFoundException, InterruptedException {
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

    private void draw() throws FileNotFoundException, InterruptedException {
        // CLEAR SCREEN
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, Config.getScreenSize().getX(), Config.getScreenSize().getY());

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

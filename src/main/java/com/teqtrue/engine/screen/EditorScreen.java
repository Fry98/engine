package com.teqtrue.engine.screen;

import java.io.FileNotFoundException;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.utils.ImageScaler;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EditorScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private int frameCount = 0;
    private int framerate = 0;
    private Coordinates coords = new Coordinates(0, 0);
    private Image sprites;

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;
        try {
            this.sprites = ImageScaler.scale("src/main/assets/env_day.png", 16, 64);
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loop() throws FileNotFoundException, InterruptedException {
        long initTime = System.currentTimeMillis();
        while (true) {
            frameCount++;
            long tickStart = System.currentTimeMillis();
            if (tickStart - initTime > 1000) {
                framerate = frameCount;
                frameCount = 0;
                initTime = tickStart;
            }
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
        if (KeyMap.isPressed(38)){
            coords.alterY(-5);
        } 
        if (KeyMap.isPressed(40)){
            coords.alterY(5);
        }
        if (KeyMap.isPressed(37)){
            coords.alterX(-5);
        }
        if (KeyMap.isPressed(39)){
            coords.alterX(5);
        }
    }

    private void draw() throws FileNotFoundException, InterruptedException {
        // CLEAR SCREEN
        gc.clearRect(0, 0, 960, 720);

        // DRAW SPRITES
        gc.drawImage(sprites, 0, 0, 64, 64, coords.getX(), coords.getY(), 64, 64);
        gc.drawImage(sprites, 64*5, 0, 64, 64, coords.getX(), coords.getY(), 64, 64);

        // DRAW FRAMERATE
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 30));
        gc.fillText(Integer.toString(framerate), 10, 40, 400);
    }
    
    @Override
    public IApplicationScreen getNextScreen() {
        return new ExitScreen();
    }

}

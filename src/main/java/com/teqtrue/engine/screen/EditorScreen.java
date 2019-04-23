package com.teqtrue.engine.screen;

import java.io.FileNotFoundException;

import com.teqtrue.engine.utils.ImageScaler;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EditorScreen implements IApplicationScreen {

    @Override
    public void init(GraphicsContext gc) {
        try {
            draw(gc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void draw(GraphicsContext gc) throws FileNotFoundException, InterruptedException {
        Image sprites = ImageScaler.init("src/main/assets/env_day.png", 4);

        // RENDER LOOP
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gc.drawImage(sprites, 0, 0, 64, 64, i * 64, j * 64, 64, 64);
                gc.drawImage(sprites, 5 * 64, 0, 64, 64, i * 64, j * 64, 64, 64);
            }
        }

        Thread.sleep(2000);
    }
    
    @Override
    public IApplicationScreen getNextScreen() {
        return new ExitScreen();
    }

}

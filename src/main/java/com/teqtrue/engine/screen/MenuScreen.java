package com.teqtrue.engine.screen;

import java.io.FileNotFoundException;

import com.teqtrue.engine.utils.ImageScaler;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MenuScreen implements IApplicationScreen {

    @Override
    public void init(GraphicsContext gc) {
        try {
            draw(gc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void draw(GraphicsContext gc) throws FileNotFoundException, InterruptedException {

    }

    @Override
    public IApplicationScreen getNextScreen() {
        return new EditorScreen();
    }

}

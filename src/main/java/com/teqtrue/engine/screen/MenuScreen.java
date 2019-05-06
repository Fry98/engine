package com.teqtrue.engine.screen;

import java.io.FileNotFoundException;

import javafx.scene.canvas.GraphicsContext;

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

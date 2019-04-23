package com.teqtrue.engine.screen;

import javafx.scene.canvas.GraphicsContext;

public class ExitScreen implements IApplicationScreen {

    @Override
    public void init(GraphicsContext gc) {
        System.exit(0);
    }

    @Override
    public IApplicationScreen getNextScreen() {
        return null;
    }
}

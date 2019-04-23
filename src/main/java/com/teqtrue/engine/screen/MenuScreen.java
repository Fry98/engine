package com.teqtrue.engine.screen;

import javafx.scene.canvas.GraphicsContext;

public class MenuScreen implements IApplicationScreen {

    @Override
    public void init(GraphicsContext gc) {

    }

    @Override
    public IApplicationScreen getNextScreen() {
        return new GameScreen();
    }

}

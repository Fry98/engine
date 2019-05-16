package com.teqtrue.engine.screen;

import javafx.scene.canvas.GraphicsContext;

public interface IApplicationScreen {

    public void init(GraphicsContext gc);
    public IApplicationScreen getNextScreen();
}

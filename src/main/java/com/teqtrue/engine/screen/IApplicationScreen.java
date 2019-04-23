package com.teqtrue.engine.screen;

import javafx.scene.canvas.GraphicsContext;

public interface IApplicationScreen {

    void init(GraphicsContext gc);
    IApplicationScreen getNextScreen();

}

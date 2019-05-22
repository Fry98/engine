package com.teqtrue.engine.screen;

import javafx.scene.canvas.GraphicsContext;

public interface IApplicationScreen {

    /**
     * Initializes a new application screen.
     * @param gc a canvas which can be used for drawing by the screen
     */
    void init(GraphicsContext gc);

    /**
     * Returns an instance of a new screen which should be shown after the current one stops.
     */
    IApplicationScreen getNextScreen();
}

package com.teqtrue.engine;

import com.teqtrue.engine.screen.EditorScreen;
import com.teqtrue.engine.screen.IApplicationScreen;
import com.teqtrue.engine.screen.MenuScreen;

import javafx.scene.canvas.GraphicsContext;

public class MainLoop implements Runnable {
    private GraphicsContext gc;

    public MainLoop(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void run() {
        IApplicationScreen screen = new EditorScreen();
        while (true) {
            screen.init(gc);
            screen = screen.getNextScreen();
        }
    }
}

package com.teqtrue.engine;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.screen.IApplicationScreen;

import com.teqtrue.engine.screen.MenuScreen;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

public class MainLoop implements Runnable {
    private GraphicsContext gc;

    public MainLoop(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void run() {
        IApplicationScreen screen = new MenuScreen();
        Config.init();
        while (true) {
            KeyMap.clear();
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.BASELINE);
            screen.init(gc);
            screen = screen.getNextScreen();
        }
    }
}

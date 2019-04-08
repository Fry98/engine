package com.teqtrue.engine.screen;

public class ExitScreen implements IApplicationScreen {

    @Override
    public void init() {
        System.exit(0);
    }

    @Override
    public void update() {}

    @Override
    public IApplicationScreen getNextScreen() {
        return null;
    }
}

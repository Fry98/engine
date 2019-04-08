package com.teqtrue.engine.screen;

public class MenuScreen implements IApplicationScreen {

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public IApplicationScreen getNextScreen() {
        return new GameScreen();
    }

}

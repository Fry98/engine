package com.teqtrue.engine.screen;

public interface IApplicationScreen {

    void init();
    void update();
    IApplicationScreen getNextScreen();

}

package com.teqtrue.engine.screen;

import com.teqtrue.engine.model.GameMap;

public interface IMapLoaderScreen extends IApplicationScreen {
    public void loadMapData(GameMap map);
}

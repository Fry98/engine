package com.teqtrue.engine.utils;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.instances.BasicEnemy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileUtilTest {

    private final String MAP_NAME ="test.map";

    @Test
    void saveLoadTest() {
        // mock GameMap
        GameMap gameMap = new GameMap();
        gameMap.setName("Test Map Name");
        gameMap.setSpawn(2, 3);
        gameMap.addEntity(new BasicEnemy(new Coordinates(1, 2)));
        gameMap.addEntity(new BasicEnemy(new Coordinates(3, 4)));
        gameMap.addProjectile(new Projectile(null, new Coordinates(1, 1), new Coordinates(2, 2)));
        gameMap.addProjectile(new Projectile(null, new Coordinates(1, 1), new Coordinates(-2, -2)));

        // save map
        FileUtil.saveObject(gameMap, MAP_NAME);
        // load map
        GameMap loadedMap = FileUtil.loadObject(MAP_NAME, GameMap.class);

        // asserts
        assertNotNull(loadedMap);
        assertEquals(loadedMap.getName(), gameMap.getName());
        assertEquals(loadedMap.getSpawn(), gameMap.getSpawn());
        assertEquals(loadedMap.getEntities(), gameMap.getEntities());
        assertEquals(loadedMap.getProjectiles(), gameMap.getProjectiles());

        // cleanup
        try {
            Files.delete(Path.of("src/main/levels/" + MAP_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
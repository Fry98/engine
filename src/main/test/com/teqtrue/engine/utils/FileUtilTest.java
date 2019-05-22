package com.teqtrue.engine.utils;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.instances.BasicEnemy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    private final String MAP_NAME = "src/main/levels/test.map";
    Random random = new Random();

    @BeforeAll
    static void init() {
        GlobalStore.init();
    }

    @Test
    void saveLoadTest() {
        // mock GameMap
        GameMap gameMap = new GameMap();
        gameMap.setName("Test Map Name");
        gameMap.setSpawn(random.nextInt(20), random.nextInt(20));
        Map<Coordinates, Integer> objects = new HashMap<>();
        for (int i = 0; i < random.nextInt(40); i++) {
            Coordinates c = new Coordinates(random.nextInt(20), random.nextInt(20));
            int obj = random.nextInt(GlobalStore.getRegisteredObjects().length);
            objects.put(c, obj);
            gameMap.set(c, obj);
        }
        for (int i = 0; i < random.nextInt(10); i++) {
            gameMap.addEntity(new BasicEnemy(new Coordinates(random.nextInt(20), random.nextInt(20))));
        }
        for (int i = 0; i < random.nextInt(10); i++) {
            gameMap.addProjectile(new Projectile(
                    null,
                    new Coordinates(random.nextInt(20), random.nextInt(20)),
                    new Coordinates(random.nextDouble(), random.nextDouble())));
        }

        // save map
        FileUtil.saveObject(gameMap, MAP_NAME);
        // load map
        GameMap loadedMap = FileUtil.loadObject(MAP_NAME, GameMap.class);

        // asserts
        assertNotNull(loadedMap);
        assertEquals(loadedMap.getName(), gameMap.getName());
        assertEquals(loadedMap.getSpawn(), gameMap.getSpawn());

        assertEquals(loadedMap.getEntities().size(), gameMap.getEntities().size());
        assertTrue(loadedMap.getEntities().containsAll(gameMap.getEntities()));

        assertEquals(loadedMap.getProjectiles().size(), gameMap.getProjectiles().size());
        assertTrue(loadedMap.getProjectiles().containsAll(gameMap.getProjectiles()));

        for (Coordinates c : objects.keySet()) {
            assertEquals(GlobalStore.getRegisteredObjects()[objects.get(c)], gameMap.get(c));
        }

        // cleanup
        try {
            Files.delete(Path.of("src/main/levels/" + MAP_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
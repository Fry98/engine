package com.teqtrue.engine.model;

import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.GameObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap implements Serializable {
    private Map<Coordinates, Integer> objects = new HashMap<>();
    private List<IEntity> entities = new ArrayList<>();
    private Coordinates spawnPoint = null;
    private String name;
    private static final long serialVersionUID = 1L;

    // SET
    public void set(Coordinates coords, Integer tile) {
        objects.put(coords, tile);
    }

    public void set(int x, int y, Integer tile) {
        set(new Coordinates(x, y), tile);
    }

    // REMOVE
    public void remove(Coordinates coords) {
        if (objects.containsKey(coords)) {
            objects.remove(coords);
        }
    }

    public void remove(int x, int y) {
        remove(new Coordinates(x, y));
    }

    // GET
    public GameObject get(Coordinates coords) {
        if (objects.containsKey(coords)) {
            int index = objects.get(coords);
            return Config.getRegisteredObjects()[index];
        }
        return null;
    }

    public GameObject get(int x, int y) {
        return get(new Coordinates(x, y));
    }

    // SPAWN
    public void setSpawn(Coordinates coords) {
        spawnPoint = coords;
    }

    public void setSpawn(int x, int y) {
        spawnPoint = new Coordinates(x, y);
    }

    public void unsetSpawn() {
        spawnPoint = null;
    }

    public Coordinates getSpawn() {
        return spawnPoint;
    }

    // ENTITIES
    public List<IEntity> getEntities() {
        return entities;
    }

    public void addEntity(IEntity entity) {
        entities.add(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

    // LEVEL NAME
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

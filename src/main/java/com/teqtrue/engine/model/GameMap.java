package com.teqtrue.engine.model;

import com.teqtrue.engine.model.object.entity.AEntity;
import com.teqtrue.engine.model.object.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    private Map<Coordinates, ArrayList<Integer>> objects = new HashMap<>();
    private List<AEntity> entities = new ArrayList<>();
    private Coordinates spawnPoint = null;

    // SET
    public void set(Coordinates coords, ArrayList<Integer> tileData) {
        objects.put(coords, tileData);
    }

    public void set(int x, int y, ArrayList<Integer> tileData) {
        set(new Coordinates(x, y), tileData);
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

    // PUSH
    public void push(Coordinates coords, int objIndex) {
        if (objects.containsKey(coords)) {
            objects.get(coords).add(objIndex);
        } else {
            ArrayList<Integer> newArray = new ArrayList<>();
            newArray.add(objIndex);
            objects.put(coords, newArray);
        }
    }
    
    public void push(int x, int y, int objIndex) {
        push(new Coordinates(x, y), objIndex);
    }

    // POP
    public void pop(Coordinates coords) {
        if (objects.containsKey(coords)) {
            ArrayList<Integer> tile = objects.get(coords);
            if (tile.size() > 1) {
                tile.remove(tile.size() - 1);
            } else {
                objects.remove(coords);
            }
        }
    }

    public void pop(int x, int y) {
        pop(new Coordinates(x, y));
    }

    // GET
    public ArrayList<GameObject> get(Coordinates coords) {
        if (objects.containsKey(coords)) {
            ArrayList<Integer> indexList = objects.get(coords);
            ArrayList<GameObject> objList = new ArrayList<>();
            for (int item : indexList) {
                objList.add(Config.getRegisteredObjects()[item]);
            }
            return objList;
        }
        return null;
    }

    public ArrayList<GameObject> get(int x, int y) {
        return get(new Coordinates(x, y));
    }

    // SET SPAWN
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

    public List<AEntity> getEntities() {
        return entities;
    }
}

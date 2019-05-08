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

    public void set(int x, int y, ArrayList<Integer> tileData) {
        objects.put(new Coordinates(x, y), tileData);
    }

    public void remove(int x, int y) {
        Coordinates coords = new Coordinates(x, y);
        if (objects.containsKey(coords)) {
            objects.remove(coords);
        }
    }

    public void push(int x, int y, int objIndex) {
        Coordinates coords = new Coordinates(x, y);
        if (objects.containsKey(coords)) {
            objects.get(coords).add(objIndex);
        } else {
            ArrayList<Integer> newArray = new ArrayList<>();
            newArray.add(objIndex);
            objects.put(coords, newArray);
        }
    }

    public void pop(int x, int y) {
        Coordinates coords = new Coordinates(x, y);
        if (objects.containsKey(coords)) {
            ArrayList<Integer> tile = objects.get(coords);
            if (tile.size() > 1) {
                tile.remove(tile.size() - 1);
            } else {
                objects.remove(coords);
            }
        }
    }

    public ArrayList<GameObject> get(int x, int y) {
        Coordinates coords = new Coordinates(x, y);
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

    public List<AEntity> getEntities() {
        return entities;
    }
}

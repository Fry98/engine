package com.teqtrue.engine.model;

import com.teqtrue.engine.model.object.entity.AEntity;
import com.teqtrue.engine.model.object.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {

    private String name;
    private Map<Coordinates, GameObject> objects = new HashMap<>();
    private List<AEntity> entities = new ArrayList<>();

    public void set(int x, int y, GameObject obj) {
        objects.put(new Coordinates(x, y), obj);
    }

    public boolean remove(int x, int y) {
        if (objects.containsKey(new Coordinates(x, y))) {
            objects.remove(new Coordinates(x, y));
            return true;
        }
        return false;
    }

    public GameObject get(int x, int y) {
        if (objects.containsKey(new Coordinates(x, y))) {
            return objects.get(new Coordinates(x, y));
        }
        return null;
    }

}

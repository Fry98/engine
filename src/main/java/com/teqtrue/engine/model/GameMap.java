package com.teqtrue.engine.model;

import com.teqtrue.engine.model.object.entity.AEntity;
import com.teqtrue.engine.model.object.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {

    private String name;
    private Map<Coordinates, ArrayList<GameObject>> objects = new HashMap<>();
    private List<AEntity> entities = new ArrayList<>();

    public void set(int x, int y, GameObject obj) {
        if (objects.containsKey(new Coordinates(x, y))) {
            objects.get(new Coordinates(x, y)).add(obj);
        } else {
            ArrayList<GameObject> newArray = new ArrayList<>();
            newArray.add(obj);
            objects.put(new Coordinates(x, y), newArray);
        }
    }

    public boolean remove(int x, int y) {
        if (objects.containsKey(new Coordinates(x, y))) {
            objects.remove(new Coordinates(x, y));
            return true;
        }
        return false;
    }

    public ArrayList<GameObject> get(int x, int y) {
        if (objects.containsKey(new Coordinates(x, y))) {
            return objects.get(new Coordinates(x, y));
        }
        return null;
    }

}

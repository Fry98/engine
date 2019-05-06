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

    public void set(int x, int y, int objIndex) {
        if (objects.containsKey(new Coordinates(x, y))) {
            objects.get(new Coordinates(x, y)).add(objIndex);
        } else {
            ArrayList<Integer> newArray = new ArrayList<>();
            newArray.add(objIndex);
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
            ArrayList<Integer> indexList = objects.get(new Coordinates(x, y));
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

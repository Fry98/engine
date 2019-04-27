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

    /*public void set(int x, int y, int val) {
        if (this.objects.containsKey(x)) {
            HashMap<Integer, Integer> column = this.objects.get(x);
            column.put(y, val);
        } else {
            HashMap<Integer, Integer> newColumn = new HashMap<>();
            newColumn.put(y, val);
            this.objects.put(x, newColumn);
        }
    }

    public boolean remove(int x, int y) {
        if (!this.objects.containsKey(x)) {
            return false;
        }
        HashMap<Integer, Integer> column = this.objects.get(x);
        if (!column.containsKey(y)) {
            return false;
        }
        if (column.size() == 1) {
            this.objects.remove(x);
        } else {
            column.remove(y);
        }
        return true;
    }

    public int get(int x, int y) {
        if (!this.objects.containsKey(x)) {
            return -1;
        }
        HashMap<Integer, Integer> column = this.objects.get(x);
        if (!column.containsKey(y)) {
            return -1;
        }
        return column.get(y);
    }*/

}

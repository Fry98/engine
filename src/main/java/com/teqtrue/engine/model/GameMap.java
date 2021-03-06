package com.teqtrue.engine.model;

import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.model.object.Projectile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GameMap implements Serializable {
    private Map<Coordinates, Integer> objects = new HashMap<>();
    private ArrayList<IEntity> entities = new ArrayList<>();
    private HashSet<Projectile> projectiles = new HashSet<>();
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
            return GlobalStore.getRegisteredObjects()[index];
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
        return (List<IEntity>) entities.clone();
    }

    public void addEntity(IEntity entity) {
        entities.add(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

    public void removeEntity(IEntity entity) {
        for (int i = 0; i < entities.size(); i++) {
            if (entity == entities.get(i)) {
                entities.remove(i);
                break;
            }
        }
    }

    // LEVEL NAME
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // COLLISION CHECK
    public boolean hasCollision(Coordinates coords) {
        return hasCollision(coords.getX(), coords.getY());
    }

    public boolean hasCollision(double x, double y) {
        Coordinates coords = new Coordinates((int) x, (int) y);
        GameObject obj = get(coords);
        if (obj == null) {
            return false;
        }
        return obj.hasCollision();
    }

    // PROJECTILES
    public synchronized void addProjectile(Projectile newProjectile) {
        if (!hasCollision(newProjectile.getPosition())) {
            projectiles.add(newProjectile);
        }
    }

    public synchronized void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }

    public synchronized ArrayList<Projectile> getProjectiles() {
        return new ArrayList<Projectile>(projectiles);
    }
}

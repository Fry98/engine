package com.teqtrue.engine.screen;

import java.util.List;

import com.teqtrue.engine.model.*;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.entity.enemies.BasicEnemy;
import com.teqtrue.engine.utils.FileUtil;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class EditorScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private Coordinates camera = new Coordinates(0, 0);
    private GameMap gameMap = new GameMap();
    private int selectedObj = 0;
    private int selectedSpecial = 0;
    private int[] specials = new int[]{11, 8};
    private boolean selectorType = false;

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;

        // SCROLL EVENT HANDLER
        gc.getCanvas().getScene().setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                if (!selectorType && selectedObj > 0) {
                    selectedObj--;
                } else if (selectorType && selectedSpecial > 0) {
                    selectedSpecial--;
                }
            } else if (e.getDeltaY() < 0) {
                if (!selectorType && selectedObj < Config.getRegisteredObjects().length - 1) {
                    selectedObj++;
                } else if (selectorType && selectedSpecial < specials.length - 1) {
                    selectedSpecial++;
                }
            }
        });

        // SELECTOR TYPE SWITCH
        gc.getCanvas().getScene().addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode().equals(KeyCode.E) && !KeyMap.isMousePressed(MouseButton.PRIMARY) && !KeyMap.isMousePressed(MouseButton.SECONDARY)) {
                selectorType = !selectorType;
            }
        });

        // START LOOP
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loop() throws InterruptedException {
        while (true) {
            long tickStart = System.currentTimeMillis();
            if (update()) break;
            draw();
            long tickDuration = System.currentTimeMillis() - tickStart;
            long timeout = 20 - tickDuration;
            if (timeout > 0) {
                Thread.sleep(20 - tickDuration);
            }
        }
    }

    private boolean update() {
        if (KeyMap.isPressed(KeyCode.ESCAPE)) {
            return true;
        }
        int speed = 10;
        if (KeyMap.isPressed(KeyCode.SHIFT)) {
            speed = 20;
        }
        if (KeyMap.isPressed(KeyCode.W)) {
            if (camera.getY() - speed >= 0) {
                camera.alterY(-speed);
            } else {
                camera.setY(0);
            }
        } 
        if (KeyMap.isPressed(KeyCode.S)) {
            if (KeyMap.isPressed(KeyCode.CONTROL)) {
                FileUtil.saveObject(gameMap, "src/main/levels/test.map");
            } else {
                camera.alterY(speed);
            }
        }
        if (KeyMap.isPressed(KeyCode.A)) {
            if (camera.getX() - speed >= 0) {
                camera.alterX(-speed);
            } else {
                camera.setX(0);
            }
        }
        if (KeyMap.isPressed(KeyCode.D)) {
            camera.alterX(speed);
        }

        if (KeyMap.isPressed(KeyCode.CONTROL) && KeyMap.isPressed(KeyCode.L)) {
            gameMap = FileUtil.loadObject("src/main/levels/test.map", GameMap.class);
        }

        Coordinates mousePos = KeyMap.getMouse();
        int tileX = (int) Math.floor((camera.getX() + mousePos.getX()) / Config.getTileSize());
        int tileY = (int) Math.floor((camera.getY() + mousePos.getY()) / Config.getTileSize());
        Coordinates tile = new Coordinates(tileX, tileY);
        if (KeyMap.isMousePressed(MouseButton.PRIMARY) && KeyMap.isMouseSinglePress()) {
            if (!selectorType) {
                gameMap.set(tile, selectedObj);
            } else if (selectorType) {
                handleSpecial(true, tile);
            }
        } else if (KeyMap.isMousePressed(MouseButton.SECONDARY) && KeyMap.isMouseSinglePress()) {
            if (!selectorType) {
                gameMap.remove(tile);
            } else if (selectorType) {
                handleSpecial(false, tile);
            }
        }

        return false;
    }

    private void draw() {
        // CLEAR SCREEN
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, Config.getScreenSize().getX(), Config.getScreenSize().getY());

        // DRAW OBJECTS IN GAMEMAP
        int leftX = (int) Math.floor(camera.getX() / Config.getTileSize());
        int upY = (int) Math.floor(camera.getY() / Config.getTileSize());
        int rightX = (int) Math.floor((camera.getX() + Config.getScreenSize().getX()) / Config.getTileSize());
        int downY = (int) Math.floor((camera.getY() + Config.getScreenSize().getY()) / Config.getTileSize());

        for (int x = leftX; x <= rightX; x++) {
            for (int y = upY; y <= downY; y++) {
                GameObject obj = gameMap.get(x, y);
                if (obj != null) {
                    double cornerX = ((x * Config.getTileSize()) - camera.getX());
                    double cornerY = ((y * Config.getTileSize()) - camera.getY());
                    obj.drawObject(gc, cornerX, cornerY);
                }
            }
        }

        // SELECTED TILE CALCULATION
        Coordinates mousePos = KeyMap.getMouse();
        int tileX = (int) Math.floor((camera.getX() + mousePos.getX()) / Config.getTileSize());
        int tileY = (int) Math.floor((camera.getY() + mousePos.getY()) / Config.getTileSize());
        int cornerX = (int) ((tileX * Config.getTileSize()) - camera.getX());
        int cornerY = (int) ((tileY * Config.getTileSize()) - camera.getY());

        // DRAW SELECTED OBJECT
        gc.setGlobalAlpha(0.7);
        if (!selectorType) {
            Config.getRegisteredObjects()[selectedObj].drawObject(gc, cornerX, cornerY);
        } else {
            Config.getSprites()[specials[selectedSpecial]].drawSprite(gc, cornerX, cornerY, 0);
        }
        gc.setGlobalAlpha(1);

        // DRAW HIGHLIGHTER
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(cornerX + 1, cornerY + 1, 47, 47);

        // DRAW SPAWN POINT
        Coordinates spawn = gameMap.getSpawn();
        if (spawn != null && spawn.getX() >= leftX && spawn.getX() <= rightX && spawn.getY() >= upY && spawn.getY() <= downY) {
            Config.getSprites()[11].drawSprite(gc, ((spawn.getX() * Config.getTileSize()) - camera.getX()), ((spawn.getY() * Config.getTileSize()) - camera.getY()), 0);
        }

        // DRAW ENTITITES
        List<IEntity> entities = gameMap.getEntities();
        for (IEntity entity : entities) {
            Coordinates entityCoords = entity.getCoordinates();
            if (entityCoords.getX() >= leftX && entityCoords.getX() <= rightX && entityCoords.getY() >= upY && entityCoords.getY() <= downY) {
                entity.getSprite().drawSprite(gc, ((entityCoords.getX() * Config.getTileSize()) - camera.getX()), ((entityCoords.getY() * Config.getTileSize()) - camera.getY()), 0);
            }
        }

        // DRAW COORDINATES
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font("Arial", 15));
        gc.fillText(String.format("X: %d, Y: %d", tileX, tileY), 5, 15);
    }

    private void handleSpecial(boolean leftClick, Coordinates tile) {
        if (leftClick) {
            switch (selectedSpecial) {
                case 0:
                    gameMap.setSpawn(tile);
                    break;
                default:
                    for (IEntity entity : gameMap.getEntities()) {
                        if (entity.getCoordinates().equals(tile)) {
                            return;
                        }
                    }
                    gameMap.addEntity(new BasicEnemy(tile, specials[selectedSpecial]));
                    break;
            }
        } else {
            if (gameMap.getSpawn() != null && gameMap.getSpawn().equals(tile)) {
                gameMap.unsetSpawn();
                return;
            }
            List<IEntity> entities = gameMap.getEntities();
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i).getCoordinates().equals(tile)) {
                    gameMap.removeEntity(i);
                }
            }
        }
    }
    
    @Override
    public IApplicationScreen getNextScreen() {
        return new MenuScreen();
    }
}

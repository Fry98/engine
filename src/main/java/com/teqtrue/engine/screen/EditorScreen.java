package com.teqtrue.engine.screen;

import java.util.List;

import com.teqtrue.engine.model.*;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.entity.instances.BasicEnemy;
import com.teqtrue.engine.utils.SaveScreen;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.event.EventHandler;

public class EditorScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private Coordinates camera = new Coordinates(0, 0);
    private GameMap gameMap;
    private int selectedObj = 0;
    private int selectedSpecial = 0;
    private int[] specials = new int[] {11, 8};
    private boolean selectorType = false;
    private boolean freeze = false;
    private boolean die = false;

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;
        gameMap = GlobalStore.getMap();

        // CAMERA SETUP
        if (gameMap.getSpawn() != null) {
            double cameraX = (gameMap.getSpawn().getX() * GlobalStore.getTileSize()) - GlobalStore.getScreenSize().getX() / 2 + GlobalStore.getTileSize() / 2;
            double cameraY = (gameMap.getSpawn().getY() * GlobalStore.getTileSize()) - GlobalStore.getScreenSize().getY() / 2 + GlobalStore.getTileSize() / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraY < 0) cameraY = 0;
            camera = new Coordinates(cameraX, cameraY);
        }

        // SCROLL EVENT HANDLER
        gc.getCanvas().getScene().setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                if (!selectorType && selectedObj > 0) {
                    selectedObj--;
                } else if (selectorType && selectedSpecial > 0) {
                    selectedSpecial--;
                }
            } else if (e.getDeltaY() < 0) {
                if (!selectorType && selectedObj < GlobalStore.getRegisteredObjects().length - 1) {
                    selectedObj++;
                } else if (selectorType && selectedSpecial < specials.length - 1) {
                    selectedSpecial++;
                }
            }
        });

        // SELECTOR TYPE SWITCH
        EventHandler<KeyEvent> keypressHandler = new EventHandler<>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.E) && !KeyMap.isMousePressed(MouseButton.PRIMARY) && !KeyMap.isMousePressed(MouseButton.SECONDARY)) {
                    selectorType = !selectorType;
                }
            }
        };

        gc.getCanvas().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keypressHandler);

        // START LOOP
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // REMOVE EVENT HANDLER
        gc.getCanvas().getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keypressHandler);
    }

    private void loop() throws InterruptedException {
        Platform.runLater(() ->
            new AnimationTimer() {
                public void handle(long currentNanoTime) {
                    if (!freeze) draw();
                    if (die) this.stop();
                }
            }.start()
        );
        while (true) {
            long tickStart = System.currentTimeMillis();
            if (!freeze) update();
            long tickDuration = System.currentTimeMillis() - tickStart;
            long timeout = 20 - tickDuration;
            if (timeout > 0) {
                Thread.sleep(20 - tickDuration);
            }
            if (die) break;
        }
    }

    private void update() {
        if (KeyMap.isPressed(KeyCode.ESCAPE)) {
            die = true;
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
                if (gameMap.getSpawn() != null) {
                    freeze();
                    Platform.runLater(() -> {
                        SaveScreen.showDialog(this);
                    });
                }
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

        Coordinates mousePos = KeyMap.getMouse();
        int tileX = (int) Math.floor((camera.getX() + mousePos.getX()) / GlobalStore.getTileSize());
        int tileY = (int) Math.floor((camera.getY() + mousePos.getY()) / GlobalStore.getTileSize());
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
    }

    private void draw() {
        // CLEAR SCREEN
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, GlobalStore.getScreenSize().getX(), GlobalStore.getScreenSize().getY());

        // DRAW OBJECTS IN GAMEMAP
        int leftX = (int) Math.floor(camera.getX() / GlobalStore.getTileSize());
        int upY = (int) Math.floor(camera.getY() / GlobalStore.getTileSize());
        int rightX = (int) Math.floor((camera.getX() + GlobalStore.getScreenSize().getX()) / GlobalStore.getTileSize());
        int downY = (int) Math.floor((camera.getY() + GlobalStore.getScreenSize().getY()) / GlobalStore.getTileSize());

        for (int x = leftX; x <= rightX; x++) {
            for (int y = upY; y <= downY; y++) {
                GameObject obj = gameMap.get(x, y);
                if (obj != null) {
                    double cornerX = ((x * GlobalStore.getTileSize()) - camera.getX());
                    double cornerY = ((y * GlobalStore.getTileSize()) - camera.getY());
                    obj.drawObject(gc, cornerX, cornerY);
                }
            }
        }

        // SELECTED TILE CALCULATION
        Coordinates mousePos = KeyMap.getMouse();
        int tileX = (int) Math.floor((camera.getX() + mousePos.getX()) / GlobalStore.getTileSize());
        int tileY = (int) Math.floor((camera.getY() + mousePos.getY()) / GlobalStore.getTileSize());
        int cornerX = (int) ((tileX * GlobalStore.getTileSize()) - camera.getX());
        int cornerY = (int) ((tileY * GlobalStore.getTileSize()) - camera.getY());

        // DRAW SELECTED OBJECT
        gc.setGlobalAlpha(0.7);
        if (!selectorType) {
            GlobalStore.getRegisteredObjects()[selectedObj].drawObject(gc, cornerX, cornerY);
        } else {
            GlobalStore.getSprites()[specials[selectedSpecial]].drawSprite(gc, cornerX, cornerY);
        }
        gc.setGlobalAlpha(1);

        // DRAW HIGHLIGHTER
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(cornerX + 1, cornerY + 1, 47, 47);

        // DRAW SPAWN POINT
        Coordinates spawn = gameMap.getSpawn();
        if (spawn != null && spawn.getX() >= leftX && spawn.getX() <= rightX && spawn.getY() >= upY && spawn.getY() <= downY) {
            GlobalStore.getSprites()[11].drawSprite(gc, ((spawn.getX() * GlobalStore.getTileSize()) - camera.getX()), ((spawn.getY() * GlobalStore.getTileSize()) - camera.getY()));
        }

        // DRAW ENTITITES
        List<IEntity> entities = gameMap.getEntities();
        for (IEntity entity : entities) {
            Coordinates entityCoords = entity.getCoordinates();
            if (entityCoords.getX() >= leftX && entityCoords.getX() <= rightX && entityCoords.getY() >= upY && entityCoords.getY() <= downY) {
                entity.getSprite().drawSprite(gc, ((entityCoords.getX() * GlobalStore.getTileSize()) - camera.getX()), ((entityCoords.getY() * GlobalStore.getTileSize()) - camera.getY()));
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
            case 1:
                for (IEntity entity : gameMap.getEntities()) {
                    if (entity.getCoordinates().equals(tile)) {
                        return;
                    }
                }
                gameMap.addEntity(new BasicEnemy(tile));
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

    private void freeze() {
        freeze = true;
        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, GlobalStore.getScreenSize().getX(), GlobalStore.getScreenSize().getY());
        gc.setGlobalAlpha(1);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        gc.setFill(Color.web("#2c59a0"));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSED", GlobalStore.getScreenSize().getX() / 2, GlobalStore.getScreenSize().getY() / 2);
    }

    public void restore() {
        freeze = false;
    }

    public GameMap getMap() {
        return gameMap;
    }
    
    @Override
    public IApplicationScreen getNextScreen() {
        return new LevelSelectScreen(new EditorScreen(), true);
    }
}

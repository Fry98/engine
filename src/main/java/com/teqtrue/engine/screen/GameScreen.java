package com.teqtrue.engine.screen;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.entity.instances.Player;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameScreen implements IMapLoaderScreen {

    private GraphicsContext gc;
    private GameMap gameMap;
    private Coordinates camera;
    private Player player;
    private boolean die;
    private double screenWidth = Config.getScreenSize().getX();
    private double screenHeight = Config.getScreenSize().getY();

    @Override
    public void init(GraphicsContext gc) {

        // GraphicsContext SETUP
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.getCanvas().getScene().setCursor(Cursor.NONE);
        this.gc = gc;

        // PLAYER SETUP
        this.player = new Player(gameMap.getSpawn());
        this.gameMap.addEntity(this.player);
        this.camera = new Coordinates(this.player.getCoordinates());

        // LOOP BOOTSTRAP
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // GraphicsContext RESET
        gc.getCanvas().getScene().setCursor(Cursor.HAND);
    }

    private void loop() throws InterruptedException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new AnimationTimer() {
                    public void handle(long currentNanoTime) {
                        draw();
                        if (die) this.stop();
                    }
                }.start();
            }
        });
        while (true) {
            long tickStart = System.currentTimeMillis();
            update();
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

        for (IEntity e : gameMap.getEntities()) {
            e.update();
        }

        // center camera on the player
        camera.setX(player.getCoordinates().getX() * Config.getTileSize() - screenWidth / 2 + Config.getTileSize() / 2.0);
        camera.setY(player.getCoordinates().getY() * Config.getTileSize() - screenHeight / 2 + Config.getTileSize() / 2.0);
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

        // DRAW LASER SIGHT
        Coordinates mousePos = KeyMap.getMouse();
        double mouseCenteredX = mousePos.getX() - screenWidth / 2;
        double mouseCenteredY = mousePos.getY() - screenHeight / 2;

        double[] dist = new double[4];
        dist[0] = mousePos.getX();
        dist[1] = mousePos.getY();
        dist[2] = screenWidth - dist[0];
        dist[3] = screenHeight - dist[1];

        int closest = -1;

        for (int i = 0; i < 4; i++) {
            if (closest == -1 || dist[i] < dist[closest]) {
                closest = i;
            }
        }

        double lineEndX = 0;
        double lineEndY = 0;
        double ratio = 0;

        switch(closest) {
            case 0:
                ratio = mouseCenteredY / mouseCenteredX;
                lineEndX = -(screenWidth / 2);
                lineEndY = lineEndX * ratio;
                break;
            case 1:
                ratio = mouseCenteredX / mouseCenteredY;
                lineEndY = -(screenHeight / 2);
                lineEndX = lineEndY * ratio;
                break;
            case 2:
                ratio = mouseCenteredY / mouseCenteredX;
                lineEndX = screenWidth / 2;
                lineEndY = lineEndX * ratio;
                break;
            case 3:
                ratio = mouseCenteredX / mouseCenteredY;
                lineEndY = screenHeight / 2;
                lineEndX = lineEndY * ratio;
                break;
        }
        
        //gc.strokeLine(screenWidth / 2, screenHeight / 2, lineEndX + screenWidth / 2, lineEndY + screenHeight / 2);

        // DRAW ENTITITES
        for (IEntity entity : gameMap.getEntities()) {
            Coordinates entityCoords = entity.getCoordinates();
            if (entityCoords.getX() >= leftX && entityCoords.getX() <= rightX && entityCoords.getY() >= upY && entityCoords.getY() <= downY) {
                entity.getSprite().drawSprite(gc, (entityCoords.getX() * Config.getTileSize()) - camera.getX(), (entityCoords.getY() * Config.getTileSize()) - camera.getY(), entity.getOrientation());
            }
        }
    
        // DRAW SCOPE
        Config.getSprites()[12].drawSprite(gc, KeyMap.getMouse().getX() - Config.getTileSize() / 2 - 1, KeyMap.getMouse().getY() - Config.getTileSize() / 2 - 1);
    }

    @Override
    public IApplicationScreen getNextScreen() {
        return new LevelSelectScreen(new GameScreen(), false);
    }

    @Override
    public void loadMapData(GameMap map) {
        gameMap = map;
    }

}

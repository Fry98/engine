package com.teqtrue.engine.screen;

import com.teqtrue.engine.model.GlobalStore;

import java.util.ArrayList;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.entity.instances.Player;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private GameMap gameMap;
    private Coordinates camera;
    private Player player;
    private boolean die;
    private double screenWidth = GlobalStore.getScreenSize().getX();
    private double screenHeight = GlobalStore.getScreenSize().getY();

    @Override
    public void init(GraphicsContext gc) {
        gameMap = GlobalStore.getMap();

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

    private void update() throws InterruptedException {
        if (KeyMap.isPressed(KeyCode.ESCAPE)) {
            die = true;
        }

        // THREADS FOR ENTITY UPDATE
        ArrayList<Thread> threadPool = new ArrayList<>();

        for (IEntity entity : gameMap.getEntities()) {
            Thread newThread = new Thread(entity.update());
            threadPool.add(newThread);
            newThread.start();
        }

        // THREADS FOR PROJECTILE UPDATE
        for (Projectile projectile : gameMap.getProjectiles()) {
            Thread newThread = new Thread(projectile.update());
            threadPool.add(newThread);
            newThread.start();
        }

        // WAIT FOR THREADS TO FINISH
        for (Thread thread : threadPool) {
            thread.join();
        }

        // center camera on the player
        camera.setX(player.getCoordinates().getX() * GlobalStore.getTileSize() - screenWidth / 2 + GlobalStore.getTileSize() / 2.0);
        camera.setY(player.getCoordinates().getY() * GlobalStore.getTileSize() - screenHeight / 2 + GlobalStore.getTileSize() / 2.0);
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
        
        gc.strokeLine(screenWidth / 2, screenHeight / 2, lineEndX + screenWidth / 2, lineEndY + screenHeight / 2);

        // DRAW ENTITITES
        for (IEntity entity : gameMap.getEntities()) {
            Coordinates entityCoords = entity.getCoordinates();
            if (entityCoords.getX() >= leftX && entityCoords.getX() <= rightX && entityCoords.getY() >= upY && entityCoords.getY() <= downY) {
                entity.getSprite().drawSprite(gc, (entityCoords.getX() * GlobalStore.getTileSize()) - camera.getX(), (entityCoords.getY() * GlobalStore.getTileSize()) - camera.getY(), entity.getOrientation());
            }
        }

        // DRAW PROJECTILES
        for (Projectile projectile : gameMap.getProjectiles()) {
            projectile.getSprite().drawSprite(
                gc,
                projectile.getPosition().getX() * GlobalStore.getTileSize() - GlobalStore.getTileSize() / 2 - camera.getX(),
                projectile.getPosition().getY() * GlobalStore.getTileSize() - GlobalStore.getTileSize() / 2 - camera.getY()
            );
        }
    
        // DRAW SCOPE
        GlobalStore.getSprites()[12].drawSprite(gc, KeyMap.getMouse().getX() - GlobalStore.getTileSize() / 2 - 1, KeyMap.getMouse().getY() - GlobalStore.getTileSize() / 2 - 1);
    }

    @Override
    public IApplicationScreen getNextScreen() {
        return new LevelSelectScreen(new GameScreen(), false);
    }

}

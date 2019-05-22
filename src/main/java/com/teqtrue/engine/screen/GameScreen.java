package com.teqtrue.engine.screen;

import com.teqtrue.engine.model.GlobalStore;

import java.util.ArrayList;
import java.util.List;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.model.object.Projectile;
import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.entity.instances.Player;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private GameMap gameMap;
    private Coordinates camera;
    private Player player;
    private boolean die;
    private double screenWidth = GlobalStore.getScreenSize().getX();
    private double screenHeight = GlobalStore.getScreenSize().getY();
    private EndScreen endScreen = EndScreen.NONE;
    private boolean laser = false;

    @Override
    public void init(GraphicsContext gc) {
        gameMap = GlobalStore.getMap();

        // GraphicsContext SETUP
        gc.getCanvas().getScene().setCursor(Cursor.NONE);
        this.gc = gc;

        // PLAYER SETUP
        this.player = new Player(gameMap.getSpawn());
        this.gameMap.addEntity(this.player);
        this.camera = new Coordinates(this.player.getCoordinates());

        EventHandler<KeyEvent> eventHandler = new EventHandler<>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.F)) {
                    laser = !laser;
                }
            }
        };

        // ADD LISTENER
        gc.getCanvas().getScene().addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);

        // LOOP BOOTSTRAP
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // REMOVE LISTENER
        gc.getCanvas().getScene().removeEventHandler(KeyEvent.KEY_PRESSED, eventHandler);

        // GraphicsContext RESET
        gc.getCanvas().getScene().setCursor(Cursor.HAND);
    }

    private void loop() throws InterruptedException {
        Platform.runLater(() -> new AnimationTimer() {
            public void handle(long currentNanoTime) {
                draw();
                if (die)
                    this.stop();
            }
        }.start());
        while (true) {
            long tickStart = System.currentTimeMillis();
            update();
            long tickDuration = System.currentTimeMillis() - tickStart;
            long timeout = 20 - tickDuration;
            if (timeout > 0) {
                Thread.sleep(20 - tickDuration);
            }
            if (die)
                break;
        }
    }

    private void update() throws InterruptedException {
        if (KeyMap.isPressed(KeyCode.ESCAPE)) {
            die = true;
        }

        // THREADS FOR ENTITY UPDATE
        ArrayList<Thread> threadPool = new ArrayList<>();
        ArrayList<Projectile> projectiles = gameMap.getProjectiles();
        List<IEntity> entities = gameMap.getEntities();

        for (IEntity entity : entities) {
            Thread newThread = new Thread(entity.update(this));
            threadPool.add(newThread);
            newThread.start();
        }

        // THREADS FOR PROJECTILE UPDATE
        for (Projectile projectile : projectiles) {
            Thread newThread = new Thread(projectile.update(this));
            threadPool.add(newThread);
            newThread.start();
        }

        // CENTER CAMERA ON PLAYER
        camera.setX(player.getCoordinates().getX() * GlobalStore.getTileSize() - screenWidth / 2
                + GlobalStore.getTileSize() / 2.0);
        camera.setY(player.getCoordinates().getY() * GlobalStore.getTileSize() - screenHeight / 2
                + GlobalStore.getTileSize() / 2.0);

        // WAIT FOR THREADS TO FINISH
        for (Thread thread : threadPool) {
            thread.join();
        }

        if (entities.size() == 1) {
            kill(true);
            return;
        }
    }

    private void draw() {
        // CLEAR SCREEN
        gc.setFill(Color.web("141d1c"));
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
        if (laser) {
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
    
            switch (closest) {
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
    
            gc.setStroke(Color.RED);
            gc.setLineWidth(1);
            gc.strokeLine(screenWidth / 2, screenHeight / 2, lineEndX + screenWidth / 2, lineEndY + screenHeight / 2);    
        }

        // DRAW ENTITITES
        for (IEntity entity : gameMap.getEntities()) {
            Coordinates entityCoords = entity.getCoordinates();
            if (entityCoords.getX() >= leftX && entityCoords.getX() <= rightX && entityCoords.getY() >= upY
                    && entityCoords.getY() <= downY) {
                entity.getSprite().drawSprite(gc, (entityCoords.getX() * GlobalStore.getTileSize()) - camera.getX(),
                        (entityCoords.getY() * GlobalStore.getTileSize()) - camera.getY(), entity.getOrientation());
            }
        }

        // DRAW PROJECTILES
        for (Projectile projectile : gameMap.getProjectiles()) {
            projectile.getSprite().drawSprite(gc,
                    projectile.getPosition().getX() * GlobalStore.getTileSize() - GlobalStore.getTileSize() / 2
                            - camera.getX(),
                    projectile.getPosition().getY() * GlobalStore.getTileSize() - GlobalStore.getTileSize() / 2
                            - camera.getY());
        }

        // DRAW HEALTH BAR
        double screen = GlobalStore.getScreenSize().getX() - 80;
        double unit = screen / player.getMaxHealth();
        double greenLength = player.getHealth() * unit;
        gc.setFill(Color.GREEN);
        gc.fillRect(40, 15, greenLength, 30);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        if (player.getHealth() < 100) {
            gc.setFill(Color.RED);
            gc.fillRect(greenLength + 40, 15, screen - greenLength, 30);
            gc.strokeLine(greenLength + 40, 15, greenLength + 40, 45);
        }
        gc.strokeRect(40, 15, screen, 30);

        // DRAW HEALTH BAR ABOVE EACH ENEMY
        for (IEntity entity : gameMap.getEntities()) {
            if (entity instanceof Player) {
                continue;
            }
            unit = 1.0 * GlobalStore.getTileSize() / entity.getMaxHealth();
            greenLength = entity.getHealth() * unit;
            Coordinates entityCoords = entity.getCoordinates();
            double x = entityCoords.getX() * GlobalStore.getTileSize() - camera.getX();
            double y = entityCoords.getY() * GlobalStore.getTileSize() - camera.getY() - 10;
            gc.setFill(Color.GREEN);
            gc.fillRect(x, y, greenLength, 5);
            gc.setFill(Color.RED);
            gc.fillRect(x + greenLength, y, GlobalStore.getTileSize() - greenLength, 5);
        }

        // DRAW SCOPE
        GlobalStore.getSprites()[12].drawSprite(
            gc,
            KeyMap.getMouse().getX() - GlobalStore.getTileSize() / 2 - 1,
            KeyMap.getMouse().getY() - GlobalStore.getTileSize() / 2 - 1
        );

        // DRAW END SCREEN
        if (endScreen != EndScreen.NONE) {
            gc.setGlobalAlpha(0.5);
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, GlobalStore.getScreenSize().getX(), GlobalStore.getScreenSize().getY());
            gc.setGlobalAlpha(1);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 50));
            switch (endScreen) {
                case WINNER:
                    gc.setFill(Color.GREEN);
                    gc.fillText("YOU WIN!", GlobalStore.getScreenSize().getX() / 2, GlobalStore.getScreenSize().getY() / 2);
                    break;
                case LOSER:
                    gc.setFill(Color.RED);
                    gc.fillText("YOU LOSE!", GlobalStore.getScreenSize().getX() / 2, GlobalStore.getScreenSize().getY() / 2);
                    break;
            }
        }
    }

    @Override
    public IApplicationScreen getNextScreen() {
        return new LevelSelectScreen(new GameScreen(), false);
    }

    public void kill(boolean winner) {
        if (winner) {
            endScreen = EndScreen.WINNER;
        } else {
            endScreen = EndScreen.LOSER;
        }
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        die = true;
    }

    public Player getPlayer() {
        return player;
    }

    private enum EndScreen {
        WINNER,
        LOSER,
        NONE
    }
}

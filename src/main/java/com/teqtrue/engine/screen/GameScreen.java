package com.teqtrue.engine.screen;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.model.object.GameObject;
import com.teqtrue.engine.model.object.entity.IEntity;
import com.teqtrue.engine.model.object.entity.Player;
import com.teqtrue.engine.utils.FileUtil;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private GameMap gameMap;
    private Coordinates camera;
    private Player player;

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;
        this.gameMap = FileUtil.loadObject("src/main/levels/test.map", GameMap.class);
        this.player = (Player) this.gameMap.getEntities().stream().filter(e -> e instanceof Player).findAny().orElse(null);
        if (this.player == null) {
            this.player = new Player(gameMap.getSpawn());
            this.gameMap.addEntity(this.player);
        }
        this.camera = this.player.getCoordinates();

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

        for (IEntity e : gameMap.getEntities()) {
            e.update();
        }

        camera.setX(player.getCoordinates().getX() * Config.getTileSize() - Config.getScreenSize().getX()/2);
        camera.setY(player.getCoordinates().getY() * Config.getTileSize() - Config.getScreenSize().getY()/2);

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

        // DRAW ENTITITES
        for (IEntity entity : gameMap.getEntities()) {
            Coordinates entityCoords = entity.getCoordinates();
            System.out.printf("%s %.1f\n", entityCoords, entity.getOrientation());
            if (entityCoords.getX() >= leftX && entityCoords.getX() <= rightX && entityCoords.getY() >= upY && entityCoords.getY() <= downY) {
                entity.getSprite().drawSprite(gc, ((entityCoords.getX() * Config.getTileSize()) - camera.getX()), ((entityCoords.getY() * Config.getTileSize()) - camera.getY()), entity.getOrientation());
            }
        }
        System.out.println();
    }

    @Override
    public IApplicationScreen getNextScreen() {
        return new MenuScreen();
    }

}

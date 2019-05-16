package com.teqtrue.engine.screen;

import java.io.File;
import java.util.ArrayList;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.utils.FileUtil;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class LevelSelectScreen implements IApplicationScreen {
    private IMapLoaderScreen nextScreen;
    private boolean goBack = false;
    private GraphicsContext gc;
    private boolean die = false;
    private ArrayList<GameMap> levels = new ArrayList<>();
    private double screenWidth =  Config.getScreenSize().getX();
    private int cardOffsetY = 100;
    private int selectedCard = -1;
    private boolean createNew;

    public LevelSelectScreen(IMapLoaderScreen nextScreen, boolean createNew) {
        this.nextScreen = nextScreen;
        this.createNew = createNew;
    }

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;

        File folder = new File("src/main/levels");
        File[] files = folder.listFiles();

        for (File file : files) {
            if (!file.getName().endsWith(".map")) continue;
            levels.add(FileUtil.loadObject(file.getName(), GameMap.class));
        }

        // START LOOP
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        // ESCAPE
        if (KeyMap.isPressed(KeyCode.ESCAPE)) {
            goBack = true;
            die = true;
        }

        // BUTTON COLLISIONS
        Coordinates mousePos = KeyMap.getMouse();
        int cardPositionY = cardOffsetY;
        selectedCard = -1;
        for (int i = 0; i < levels.size(); i++) {
            if (mousePos.getX() > 40 && mousePos.getX() < screenWidth - 40 && mousePos.getY() > cardPositionY && mousePos.getY() < cardPositionY + 60) {
                selectedCard = i;
                break;
            }
            cardPositionY += 70;
        }
        if (selectedCard == -1 && createNew) {
            if (mousePos.getX() > 350 && mousePos.getX() < screenWidth - 350 && mousePos.getY() > cardPositionY && mousePos.getY() < cardPositionY + 50) {
                selectedCard = -2;
            }
        }

        // BUTTON CLICK HANDLERS
        if (KeyMap.isMousePressed(MouseButton.PRIMARY) && selectedCard != -1) {
            die = true;
        }
    }

    private void draw() {

        // CLEAR SCREEN
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillRect(0, 0, Config.getScreenSize().getX(), Config.getScreenSize().getY());
        
        // TITLE
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.setFont(Font.font("Arial", 50));
        gc.fillText("Level Select", gc.getCanvas().getWidth() / 2, 20);

        // LEVEL CARDS
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font("Arial", 30));
        int cardPositionY = cardOffsetY;
        for (int i = 0; i < levels.size(); i++) {
            if (i == selectedCard) {
                gc.setFill(Color.BROWN);
                gc.fillRect(40, cardPositionY, screenWidth - 80, 60);
                gc.setFill(Color.BLACK);
            }
            gc.strokeRect(40, cardPositionY, screenWidth - 80, 60);
            gc.fillText(levels.get(i).getName(), screenWidth / 2, cardPositionY + 30);
            cardPositionY += 70;
        }
        if (createNew) {
            if (selectedCard == -2) {
                gc.setFill(Color.BROWN);
                gc.fillRect(350, cardPositionY, screenWidth - 700, 50);
                gc.setFill(Color.BLACK);
            }
            gc.strokeRect(350, cardPositionY, screenWidth - 700, 50);
            gc.fillText("Create New Level", screenWidth / 2, cardPositionY + 25);
        }
    }

    @Override
    public IApplicationScreen getNextScreen() {
        if (goBack) {
            return new MenuScreen();
        }
        if (selectedCard > -1) {
            nextScreen.loadMapData(levels.get(selectedCard));
        } else {
            nextScreen.loadMapData(new GameMap());
        }
        return nextScreen;
    }
}

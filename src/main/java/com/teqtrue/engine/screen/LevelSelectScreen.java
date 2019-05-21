package com.teqtrue.engine.screen;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.utils.FileUtil;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class LevelSelectScreen implements IApplicationScreen {
    private IApplicationScreen nextScreen;
    private GraphicsContext gc;
    private boolean die = false;
    private ArrayList<GameMap> levels = new ArrayList<>();
    private double screenWidth =  GlobalStore.getScreenSize().getX();
    private int cardOffsetY = 100;
    private int selectedCard = -1;
    private boolean createNew;    
    private boolean delete = false;
    private ArrayList<File> files;

    public LevelSelectScreen(IApplicationScreen nextScreen, boolean createNew) {
        this.nextScreen = nextScreen;
        this.createNew = createNew;
    }

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;

        File folder = new File("src/main/levels");
        files = new ArrayList<>(Arrays.asList(folder.listFiles()));

        for (int i = 0; i < files.size(); i++) {
            if (!files.get(i).getName().endsWith(".map")) {
                files.remove(i);
                i--;
                continue;
            }
            levels.add(FileUtil.loadObject(files.get(i).getName(), GameMap.class));
        }

        // START LOOP
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loop() throws InterruptedException {
        Platform.runLater(() ->
            new AnimationTimer() {
                public void handle(long currentNanoTime) {
                    draw();
                    if (die) this.stop();
                }
            }.start()
        );
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
            nextScreen = new MenuScreen();
            die = true;
        }

        // BUTTON COLLISIONS
        Coordinates mousePos = KeyMap.getMouse();
        int cardPositionY = cardOffsetY;
        selectedCard = -1;
        delete = false;
        for (int i = 0; i < levels.size(); i++) {
            if (mousePos.getX() > 40 && mousePos.getX() < screenWidth - 40 && mousePos.getY() > cardPositionY && mousePos.getY() < cardPositionY + 60) {
                selectedCard = i;
                if (mousePos.getX() > screenWidth - 95 && mousePos.getX() < screenWidth - 45 && mousePos.getY() > cardPositionY + 5 && mousePos.getY() < cardPositionY + 55) {
                    delete = true;
                }
                break;
            }
            cardPositionY += 70;
        }
        if (selectedCard == -1 && createNew) {
            if (mousePos.getX() > 350 && mousePos.getX() < screenWidth - 350 && mousePos.getY() > cardPositionY && mousePos.getY() < cardPositionY + 50) {
                selectedCard = -2;
            }
        }

        // MOUSE CLICK HANDLING
        if (KeyMap.wasMouseClickedNow()) {
            if (selectedCard != -1) {
                if (delete) {
                    removeLevel(selectedCard);
                } else {
                    die = true;
                }
            }
        }
    }

    private void draw() {

        // CLEAR SCREEN
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillRect(0, 0, GlobalStore.getScreenSize().getX(), GlobalStore.getScreenSize().getY());
        
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
            if (i == selectedCard && !delete) {
                gc.setFill(Color.BROWN);
                gc.fillRect(40, cardPositionY, screenWidth - 80, 60);
                gc.setFill(Color.BLACK);
            }
            gc.strokeRect(40, cardPositionY, screenWidth - 80, 60);
            if (i == selectedCard) {
                if (delete) {
                    gc.setFill(Color.BROWN);
                } else {
                    gc.setFill(Color.PAPAYAWHIP);
                }
                gc.fillRect(screenWidth - 95, cardPositionY + 5, 50, 50);
                gc.setFill(Color.BLACK);
            }
            gc.strokeRect(screenWidth - 95, cardPositionY + 5, 50, 50);
            gc.strokeLine(screenWidth - 85, cardPositionY + 15, screenWidth - 55, cardPositionY + 45);
            gc.strokeLine(screenWidth - 55, cardPositionY + 15, screenWidth - 85, cardPositionY + 45);
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

    private void removeLevel(int lvlIndex) {
        files.get(lvlIndex).delete();
        levels.remove(lvlIndex);
        files.remove(lvlIndex);
    }

    @Override
    public IApplicationScreen getNextScreen() {
        if (selectedCard > -1) {
            GlobalStore.setMap(levels.get(selectedCard));
        } else {
            GlobalStore.setMap(new GameMap());
        }
        return nextScreen;
    }
}

package com.teqtrue.engine.screen;

import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.KeyMap;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class MenuScreen implements IApplicationScreen {

    private GraphicsContext gc;
    private IApplicationScreen nextScreen;
    private String[] menuItems = { "Start Game", "Editor", "Quit" };
    private int menuItemSelected;
    private boolean die = false;

    @Override
    public void init(GraphicsContext gc) {
        this.gc = gc;
        this.nextScreen = null;
        this.menuItemSelected = -1;

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
        Coordinates screenSize = GlobalStore.getScreenSize();
        Coordinates mousePos = KeyMap.getMouse();

        // MENU ITEM HIGHLIGHT
        if (mousePos.getX() > screenSize.getX() * 0.35 && mousePos.getX() < screenSize.getX() * 0.65) {
            if (mousePos.getY() < screenSize.getY() / 2 - 40
                    || mousePos.getY() > screenSize.getY() / 2 + 70 * (menuItems.length - 1)) {
                menuItemSelected = -1;
            } else {
                menuItemSelected = ((int) ((mousePos.getY() - screenSize.getY() / 2 + 40) / 70));
            }
        } else {
            menuItemSelected = -1;
        }
        // MENU ITEM CLICK
        if (KeyMap.isMousePressed(MouseButton.PRIMARY)) {
            KeyMap.setMousePressed(MouseButton.PRIMARY, true);
            switch (menuItemSelected) {
            case 0:
                nextScreen = new LevelSelectScreen(new GameScreen(), false);
                die = true;
                break;
            case 1:
                nextScreen = new LevelSelectScreen(new EditorScreen(), true);
                die = true;
                break;
            case 2:
                nextScreen = new ExitScreen();
                die = true;
                break;
            }
        }
    }

    private void draw() {
        Coordinates screenSize = GlobalStore.getScreenSize();

        // CLEAR SCREEN
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillRect(0, 0, screenSize.getX(), screenSize.getY());

        // TITLE
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", 70));
        gc.fillText("THREAT OF THE THREAD", gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 5);

        // BUTTONS
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        for (int i = 0; i < menuItems.length; i++) {
            if (i == menuItemSelected) {
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            } else {
                gc.setFont(Font.font("Arial", FontWeight.NORMAL, 40));
            }
            gc.fillText(menuItems[i], screenSize.getX() / 2, screenSize.getY() / 2 + i * 70);
        }
    }

    @Override
    public IApplicationScreen getNextScreen() {
        return nextScreen;
    }
}

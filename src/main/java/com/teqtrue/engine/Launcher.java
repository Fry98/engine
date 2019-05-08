package com.teqtrue.engine;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import com.teqtrue.engine.model.Config;
import com.teqtrue.engine.model.KeyMap;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws InterruptedException {

        // GAME WINDOW
        stage.setTitle("TeqEngine");
        Group root = new Group();
        Canvas canvas = new Canvas(Config.getScreenSize().getX(), Config.getScreenSize().getY());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.HAND);

        scene.setOnMouseMoved(e -> KeyMap.setMousePosition(e.getX(), e.getY()));

        scene.setOnMouseDragged(e -> KeyMap.setMousePosition(e.getX(), e.getY()));

        scene.setOnMousePressed(e -> KeyMap.setMousePressed(e.getButton(), false));
        scene.setOnMouseReleased(e -> KeyMap.setMousePressed(e.getButton(), true));

        scene.setOnKeyPressed(e -> KeyMap.setKey(e.getCode(), false));
        scene.setOnKeyReleased(e -> KeyMap.setKey(e.getCode(), true));

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // MAIN LOOP BOOTSTRAP
        new Thread(new MainLoop(gc)).start();
    }
}

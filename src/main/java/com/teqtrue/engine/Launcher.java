package com.teqtrue.engine;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws InterruptedException {

        // GAME WINDOW
        stage.setTitle("TeqEngine");
        Group root = new Group();
        Canvas canvas = new Canvas(960, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        // MAIN LOOP BOOTSTRAP
        new Thread(new MainLoop(gc)).start();
    }
}

package com.teqtrue;

import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Engine extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws FileNotFoundException {
    stage.setTitle("TeqEngine");
    Group root = new Group();
    Canvas canvas = new Canvas(960, 720);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    drawScreen(gc);
    root.getChildren().add(canvas);
    stage.setScene(new Scene(root));
    stage.setResizable(false);
    stage.show();
  }

  private void drawScreen(GraphicsContext gc) throws FileNotFoundException {
    Image sprites = ImageScaler.init("src/main/assets/env_day.png", 4);

    // RENDER LOOP
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        gc.drawImage(sprites, 0, 0, 64, 64, i*64, j*64, 64, 64);
        gc.drawImage(sprites, 5*64, 0, 64, 64, i*64, j*64, 64, 64);
      }
    }
  }
}
import java.io.FileInputStream;
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
    Canvas canvas = new Canvas(1280, 720);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    drawScreen(gc);
    root.getChildren().add(canvas);
    stage.setScene(new Scene(root));
    stage.setResizable(false);
    stage.show();
  }

  private void drawScreen(GraphicsContext gc) throws FileNotFoundException {
    Image sprites = new Image(new FileInputStream("src/main/assets/env_day.png"));
    TextureRenderer ir = new TextureRenderer(gc);

    // RENDERER SETTINGS
    int tileSize = 16;
    int scaler = 3;
    int tilesX = 10;
    int tilesY = 10;

    // RENDER LOOP
    for (int i = 0; i < tilesX; i++) {
      for (int j = 0; j < tilesY; j++) {
        ir.draw(sprites, 0, 0, tileSize, tileSize, (i*tileSize*scaler), (j*tileSize*scaler), scaler);
        ir.draw(sprites, (5*tileSize), 0, tileSize, tileSize, (i*tileSize*scaler), (j*tileSize*scaler), scaler);
      }
    }
    // gc.drawImage(sprites, 0, 0, 16, 16, 0, 0, 64, 64);
  }
}
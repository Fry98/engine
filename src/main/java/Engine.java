import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Engine extends Application {

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene scene = new Scene(root, 1280, 720);
    stage.setTitle("TeqEngine");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }
  public static void main(String[] args) {
    launch(args);
  }
}
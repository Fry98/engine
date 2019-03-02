import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class TextureRenderer {
  GraphicsContext gc;

  public TextureRenderer(GraphicsContext gc) {
    this.gc = gc;
  }

  public void draw(Image source, int sx, int sy, int sw, int sh, int tx, int ty, int scaler) {
    PixelReader reader = source.getPixelReader();
    PixelWriter writer = this.gc.getPixelWriter();
    for (int x = 0; x < sw; x++) {
      for (int y = 0; y < sh; y++) {
        Color color = reader.getColor(sx + x, sy + y);
        if (color.isOpaque()) {
          for (int i = 0; i < scaler; i++) {
            for (int j = 0; j < scaler; j++) {
              writer.setColor(tx + (x*scaler) + i, ty + (y*scaler) + j, color);
            }
          }
        }
      }
    }
  }
}
package com.teqtrue.engine.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class ImageScaler {
  public static Image scale(String path, int originalSize, int scaledSize) throws FileNotFoundException {
    Image source = new Image(new FileInputStream(path));
    double scalar = scaledSize / originalSize;
    double newWidth = source.getWidth() * scalar;
    double newHeight = source.getHeight() * scalar;
    return new Image(new FileInputStream(path), newWidth, newHeight, false, false);
  }
}

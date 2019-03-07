package com.teqtrue.engine.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class ImageScaler {
  public static Image init(String path, int scaler) throws FileNotFoundException {
    Image source = new Image(new FileInputStream(path));
    double newWidth = source.getWidth() * scaler;
    double newHeight = source.getHeight() * scaler;
    return new Image(new FileInputStream(path), newWidth, newHeight, false, false);
  }
}
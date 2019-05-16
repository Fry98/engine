package com.teqtrue.engine.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.HashSet;

public class KeyMap {
    private static HashSet<KeyCode> keys = new HashSet<>();
    private static HashSet<MouseButton> mouse = new HashSet<>();
    private static Coordinates mousePosition = new Coordinates(0, 0);

    public static void setKey(KeyCode keyCode, boolean unset) {
        if (!unset) {
            keys.add(keyCode);
        } else {
            keys.remove(keyCode);
        }
    }

    public static boolean isPressed(int keyCode) {
        return keys.stream().anyMatch(k -> k.getCode() == keyCode);
    }

    public static boolean isPressed(String keyName) {
        return keys.stream().anyMatch(k -> keyName.equals(k.getName()));
    }

    public static boolean isPressed(KeyCode keyCode) {
        return keys.contains(keyCode);
    }

    public static void setMousePressed(MouseButton button, boolean unset) {
        if (!unset) {
            mouse.add(button);
        } else {
            mouse.remove(button);
        }
    }

    public static boolean isMousePressed(MouseButton button) {
        return mouse.contains(button);
    }

    public static boolean isMouseSinglePress() {
        return (mouse.size() == 1);
    }

    public static void setMousePosition(double x, double y) {
        mousePosition.setX(x);
        mousePosition.setY(y);
    }

    public static Coordinates getMouse() {
        return mousePosition;
    }

    public static void clear() {
        keys.clear();
        mouse.clear();
    }
}

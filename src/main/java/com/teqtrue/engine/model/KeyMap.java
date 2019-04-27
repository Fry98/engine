package com.teqtrue.engine.model;

import java.util.HashSet;

public class KeyMap {
    private static HashSet<Integer> keys = new HashSet<>();
    private static Coordinates mousePosition = new Coordinates(0, 0);

    public static void setKey(int keyCode, boolean unset) {
        if (!unset) {
            keys.add(keyCode);
        } else if (keys.contains(keyCode)) {
            keys.remove(keyCode);
        }
    }

    public static boolean isPressed(int keyCode) {
        return keys.contains(keyCode);
    }

    public static void setMousePosition(double x, double y) {
        mousePosition.setX(x);
        mousePosition.setY(y);
    }

    public static Coordinates getMouse() {
        return mousePosition;
    }
}

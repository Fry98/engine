package com.teqtrue.engine.model;

import java.util.HashSet;

public class KeyMap {
    private static HashSet<Integer> keys = new HashSet<>();

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
}

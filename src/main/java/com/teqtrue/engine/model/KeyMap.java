package com.teqtrue.engine.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.HashSet;

public class KeyMap {
    private static HashSet<KeyCode> keys = new HashSet<>();
    private static HashSet<MouseButton> mouse = new HashSet<>();
    private static Coordinates mousePosition = new Coordinates(0, 0);
    private static boolean clickedNow = false;

    /**
     * Updates pressed status of the key.
     * @param keyCode key identification
     * @param unset if {@code false}, the key is pressed, if {@code true}, the key is no longer pressed
     */
    public static void setKey(KeyCode keyCode, boolean unset) {
        if (!unset) {
            keys.add(keyCode);
        } else {
            keys.remove(keyCode);
        }
    }

    /**
     * Returns {@code true} if the key is currently pressed, {@code false} otherwise.
     * @param keyCode key identification
     */
    public static boolean isPressed(int keyCode) {
        return keys.stream().anyMatch(k -> k.getCode() == keyCode);
    }

    /**
     * Returns {@code true} if the key is currently pressed, {@code false} otherwise.
     * @param keyName key identification
     */
    public static boolean isPressed(String keyName) {
        return keys.stream().anyMatch(k -> keyName.equals(k.getName()));
    }

    /**
     * Returns {@code true} if the key is currently pressed, {@code false} otherwise.
     * @param keyCode key identification
     */
    public static boolean isPressed(KeyCode keyCode) {
        return keys.contains(keyCode);
    }

    /**
     * Returns a vector on unit circle where the player should move based on WASD press.
     */
    public static Coordinates getMovementVector() {
        Coordinates vec = new Coordinates(0, 0);

        if (isPressed(KeyCode.W)) {
            vec.alterY(-1);
        }
        if (isPressed(KeyCode.S)) {
            vec.alterY(1);
        }
        if (isPressed(KeyCode.A)) {
            vec.alterX(-1);
        }
        if (isPressed(KeyCode.D)) {
            vec.alterX(1);
        }

        // keep the speed constant when moving diagonally
        if (Math.abs(vec.getX()) + Math.abs(vec.getY()) == 2) {
            vec.setX(vec.getX() * 1/Math.sqrt(2));
            vec.setY(vec.getY() * 1/Math.sqrt(2));
        }

        return vec;
    }

    /**
     * Updates pressed status of the mouse button.
     * @param button button identification
     * @param unset if {@code false}, the key is pressed, if {@code true}, the key is no longer pressed
     */
    public static void setMousePressed(MouseButton button, boolean unset) {
        if (!unset) {
            mouse.add(button);
        } else {
            mouse.remove(button);
        }
        if (button.equals(MouseButton.PRIMARY) && !unset) {
            clickedNow = true;
        }
    }

    /**
     * Returns {@code true} if the mouse button is currently pressed, {@code false} otherwise.
     * @param button mouse button identification
     */
    public static boolean isMousePressed(MouseButton button) {
        return mouse.contains(button);
    }

    /**
     * Returns {@code true} if and only if exactly one mouse button is pressed at the time, {@code false} otherwise.
     */
    public static boolean isMouseSinglePress() {
        return (mouse.size() == 1);
    }

    public static boolean wasMouseClickedNow() {
        if (clickedNow) {
            clickedNow = false;
            return true;
        }
        return false;
    }

    /**
     * Updates recorded position of the mouse cursor
     * @param x first coordinate
     * @param y second coordinate
     */
    public static void setMousePosition(double x, double y) {
        mousePosition.setX(x);
        mousePosition.setY(y);
    }

    /**
     * Returns current position of the mouse cursor.
     */
    public static Coordinates getMouse() {
        return mousePosition;
    }

    /**
     * Clears all key and mouse press information.
     */
    public static void clear() {
        keys.clear();
        mouse.clear();
        clickedNow = false;
    }
}

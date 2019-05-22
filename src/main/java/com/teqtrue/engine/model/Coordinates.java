package com.teqtrue.engine.model;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {

    private double x, y;
    private static final long serialVersionUID = 1L;

    /**
     * Creates a copy of another {@code Coordinates} object.
     * @param other coordinates which will be copied to this instance
     */
    public Coordinates(Coordinates other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    /**
     * Creates new {@code Coordinates} from a coordinate pair.
     * @param x position on the first axis
     * @param y position on the second axis
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns position on the first axis.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets position on the first axis.
     * @param x position to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns position on the second axis.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets position on the second axis.
     * @param y position to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Modifies position on the first axis.
     * @param x this number will be added to the current first axis value
     */
    public void alterX(double x) {
        this.x += x;
    }

    /**
     * Modifies position on the second axis.
     * @param y this number will be added to the current second axis value
     */
    public void alterY(double y) {
        this.y += y;
    }

    /**
     * Returns Euclidean distance between this and other {@code Coordinates}.
     * @param other
     */
    public double distanceFrom(Coordinates other) {
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("%.1f; %.1f", x, y);
    }

}

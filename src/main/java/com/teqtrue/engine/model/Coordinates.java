package com.teqtrue.engine.model;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {

    private double x, y;
    private static final long serialVersionUID = 1L;

    public Coordinates(Coordinates other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void alterX(double x) {
        this.x += x;
    }

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

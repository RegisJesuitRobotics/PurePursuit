package com.regisjesuit.purepursuit.utils;

import com.regisjesuit.purepursuit.path.PathPoint;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2d {
    private double x;
    private double y;

    /**
     * Gets the vector with using point1 as the origin
     *
     * @param point1 point 1 (will become the origin)
     * @param point2 point 2
     * @return the vector
     */
    public static Vector2d fromPathPoints(PathPoint point1, PathPoint point2) {
        return new Vector2d(point2.getX() - point1.getX(), point2.getY() - point1.getY());
    }

    /**
     * Gets the vector with using point1 as the origin
     *
     * @param point1 point 1 (will become the origin)
     * @param point2 point 2
     * @return the vector
     */
    public static Vector2d fromPoints(Point2d point1, Point2d point2) {
        return new Vector2d(point2.x - point1.x, point2.y - point1.y);
    }

    public Vector2d(Vector2d clone) {
        this(clone.getX(), clone.getY());
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        if (magnitude() != 0) {
            divide(magnitude());
        }
    }

    public Vector2d getNormalized() {
        Vector2d copy = new Vector2d(this);
        copy.normalize();
        return copy;
    }

    public void multiply(double multiplier) {
        x *= multiplier;
        y *= multiplier;
    }

    public void divide(double divider) {
        x /= divider;
        y /= divider;
    }

    public double dot(Vector2d vector2d) {
        return (this.x * vector2d.getX() + this.y * vector2d.getY());
    }

    public static Vector2d origin() {
        return new Vector2d(0, 0);
    }
}

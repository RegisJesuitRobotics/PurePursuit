package com.regisjesuit.purepursuit.utils;

import edu.wpi.first.wpilibj.geometry.Pose2d;

public class Point2d {
    public double x;
    public double y;

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2d(Pose2d pose2d) {
        this(pose2d.getX(), pose2d.getY());
    }

    public static double distance(Point2d point1, Point2d point2) {
        return MathUtils.distanceFormula(point1.x - point2.x, point1.y - point2.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
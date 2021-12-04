package com.regisjesuit.purepursuit.utils;

import com.regisjesuit.purepursuit.path.PathPoint;

public class MathUtils {

    private MathUtils() {}

    public static double getCurvature(PathPoint curvaturePoint, PathPoint previousPoint, PathPoint nextPoint) {
        double x1 = curvaturePoint.getX() + 0.0001; // Prevent divide by zero
        double y1 = curvaturePoint.getY();
        double x2 = previousPoint.getX();
        double y2 = previousPoint.getY();
        double x3 = nextPoint.getX();
        double y3 = nextPoint.getY();

        double k1 = 0.5 * (sqr(x1) + sqr(y1) - sqr(x2) - sqr(y2)) / (x1 - x2);
        double k2 = (y1 - y2) / (x1 - x2);

        double b = 0.5 * (sqr(x2) - 2 * x2 * k1 + sqr(y2) - sqr(x3) + 2 * x3 * k1 - sqr(y3))
                / (x3 * k2 - y3 + y2 - x2 * k2);
        double a = k1 - k2 * b;

        double r = Math.sqrt(sqr(x1 - a) + sqr(y1 - b));

        double curvature = 1 / r;

        if (Double.isNaN(curvature)) {
            return 0;
        }
        return curvature;
    }

    /**
     * @param angle1 vector 1 neither of these can be the origin
     * @param angle2 vector 2
     * @return The angle from 0 - pi in radians
     */
    public static double vectorAngle(Vector2d angle1, Vector2d angle2) {
        return Math.acos(angle1.getNormalized().dot(angle2.getNormalized()));
    }

    /**
     * @param lineStart    start of the line
     * @param lineEnd      end of the line
     * @param circleCenter center of the circle
     * @param circleRadius radius of the circle
     * @return -1 if no intersection, or 0 <= x <= 1 for where on the line it
     *         intersects the circle
     */
    public static double circleIntersectionWithLine(Point2d lineStart, Point2d lineEnd, Point2d circleCenter,
            double circleRadius) {
        // https://stackoverflow.com/questions/1073336/circle-line-segment-collision-detection-algorithm
        Vector2d d = Vector2d.fromPoints(lineStart, lineEnd);
        Vector2d f = Vector2d.fromPoints(circleCenter, lineStart);

        double a = d.dot(d);
        double b = 2 * f.dot(d);
        double c = f.dot(f) - sqr(circleRadius);

        double discriminant = sqr(b) - 4 * a * c;

        if (discriminant >= 0) {
            double rootedDiscriminant = Math.sqrt(discriminant);
            double solution1 = (-b - rootedDiscriminant) / (2 * a);
            double solution2 = (-b + rootedDiscriminant) / (2 * a);

            if (solution1 >= 0 && solution1 <= 1) {
                return solution1;
            }
            if (solution2 >= 0 && solution2 <= 1) {
                return solution2;
            }
        }
        return -1;
    }

    public static double distanceFormula(double a, double b) {
        return Math.sqrt(sqr(a) + sqr(b));
    }

    public static double sqr(double base) {
        return base * base;
    }
}

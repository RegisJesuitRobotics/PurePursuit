package com.regisjesuit.purepursuit.path;


import static com.regisjesuit.purepursuit.utils.MathUtils.sqr;

import com.regisjesuit.purepursuit.utils.MathUtils;
import com.regisjesuit.purepursuit.utils.Vector2d;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Add your docs here.
 */
public class PurePursuitPath {

    private List<PathPoint> points = new ArrayList<>();
    private final double maxVelocity;
    private final double defaultLookaheadDistance;

    public PurePursuitPath(double maxVelocity, double defaultLookaheadDistance) {
        this.maxVelocity = maxVelocity;
        this.defaultLookaheadDistance = defaultLookaheadDistance;
    }

    public void addPoint(double x, double y) {
        points.add(new PathPoint(x, y, defaultLookaheadDistance));
    }

    public void addPoint(double x, double y, double customLookaheadDistance) {
        points.add(new PathPoint(x, y, customLookaheadDistance));
    }

    public int getSize() {
        return points.size();
    }

    /**
     * @return a COPY of the list of points
     */
    public List<PathPoint> getPointsCopy() {
        List<PathPoint> copy = new ArrayList<>();
        for (PathPoint point : points) {
            copy.add(new PathPoint(point));
        }
        return copy;
    }

    public List<PathPoint> getPoints() {
        return points;
    }

    public void injectPoints() {
        this.injectPoints(0.15);
    }

    public void injectPoints(double spacing) {
        List<PathPoint> newPoints = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i++) {
            PathPoint startPoint = points.get(i);
            PathPoint endPoint = points.get(i + 1);

            Vector2d vector2d = Vector2d.fromPathPoints(startPoint, endPoint);

            double amountOfPointsToAdd = Math.ceil(vector2d.magnitude() / spacing);
            vector2d.normalize();
            double xAdditive = vector2d.getX() * spacing;
            double yAdditive = vector2d.getY() * spacing;

            int amountForStart = (int) (amountOfPointsToAdd / 2);

            for (int j = 0; j < amountForStart; j++) {
                // As we get farther from the original point add more of the additive
                newPoints.add(new PathPoint(startPoint.getX() + (xAdditive * j), startPoint.getY() + (yAdditive * j), startPoint.getLookaheadDistance()));
            }
            for (int j = amountForStart; j < amountOfPointsToAdd; j++) {
                newPoints.add(new PathPoint(startPoint.getX() + (xAdditive * j), startPoint.getY() + (yAdditive * j), endPoint.getLookaheadDistance()));
            }
        }
        newPoints.add(points.get(points.size() - 1)); // Add the last point

        points = newPoints;
    }

    /**
     * Team 2168's smoothing algorithm
     *
     * @param a         weight data (around 1 - b)
     * @param b         weight smooth: increase to get smoother path (between 0.75 -
     *                  0.98)
     * @param tolerance (around 0.001)
     */
    public void smoothPoints(double a, double b, double tolerance) {
        List<PathPoint> newPoints = getPointsCopy();

        double change = tolerance;
        while (change >= tolerance) {
            change = 0.0;
            for (int i = 1; i < newPoints.size() - 1; i++) {
                PathPoint beforeSmoothingPoint = points.get(i);
                PathPoint currentPoint = newPoints.get(i);
                PathPoint previousPoint = newPoints.get(i - 1);
                PathPoint nextPoint = newPoints.get(i + 1);

                double newX = currentPoint.getX();
                double newY = currentPoint.getY();

                newX += a * (beforeSmoothingPoint.getX() - currentPoint.getX())
                        + b * (previousPoint.getX() + nextPoint.getX() - 2 * currentPoint.getX());
                newY += a * (beforeSmoothingPoint.getY() - currentPoint.getY())
                        + b * (previousPoint.getY() + nextPoint.getY() - 2 * currentPoint.getY());

                change += Math.abs(currentPoint.getX() - newX);
                change += Math.abs(currentPoint.getY() - newY);

                currentPoint.setX(newX);
                currentPoint.setY(newY);
            }
        }

        points = newPoints;
    }

    /**
     * Calculates the curvature of a point using
     * <code>MathUtils.getCurvature()</code>
     */
    public void calculateCurvatures() {
        for (int i = 1; i < points.size() - 1; i++) {
            PathPoint currentPoint = points.get(i);
            PathPoint previousPoint = points.get(i - 1);
            PathPoint nextPoint = points.get(i + 1);

            double pointCurvature = MathUtils.getCurvature(currentPoint, previousPoint, nextPoint);
            currentPoint.setCurvature(pointCurvature);
        }
    }

    /**
     * Calculates maximum target velocity for each point using velocityConstant /
     * curvature. <code>PurePursuitPath#calculateCurvatures</code> should be called
     * before this!
     *
     * @param velocityConstant how fast a bot should go around a turn. (1-5
     *                         recommended)
     */
    public void calculateMaxVelocities(double velocityConstant) {
        for (int i = 1; i < points.size() - 1; i++) {
            PathPoint currentPoint = points.get(i);
            currentPoint.setMaxVelocity(Math.min(maxVelocity, velocityConstant / currentPoint.getCurvature()));
        }
        points.get(0).setMaxVelocity(points.get(1).getMaxVelocity());
    }

    /**
     * Calculates the target velocity for each point.
     * <code>PurePursuitPath#calculateMaxVelocities</code> should be called before
     * this!
     *
     * @param maxAcceleration The maximum acceleration in m/s
     */
    public void calculateVelocities(double maxAcceleration) {
        points.get(points.size() - 1).setVelocity(0); // Stop at end of path

        for (int i = points.size() - 2; i >= 0; i--) {
            PathPoint currentPoint = points.get(i);
            PathPoint nextPoint = points.get(i + 1);

            currentPoint.setVelocity(Math.min(currentPoint.getMaxVelocity(), Math.sqrt(
                    sqr(nextPoint.getVelocity()) + 2 * maxAcceleration * PathPoint.distance(currentPoint, nextPoint))));
        }
        System.out.println(points.get(3).getVelocity());
    }
}

package com.regisjesuit.purepursuit.path;

import com.regisjesuit.purepursuit.utils.Point2d;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import lombok.Data;

/**
 * Class used to keep data on specific point in PurePursuit path
 */
@Data
public class PathPoint {
    private double x;
    private double y;
    private double maxVelocity = 0;
    private double velocity;
    private double curvature;
    private double lookaheadDistance;

    /**
     * Creates a path point
     *
     * @param x the x coordinate (meters)
     * @param y the y coordinate (meters)
     * @param lookaheadDistance the lookahead distance for this point
     */
    public PathPoint(double x, double y, double lookaheadDistance) {
        this(x, y, lookaheadDistance, 0, 0);
    }

    public PathPoint(double x, double y, double lookaheadDistance, double velocity, double curvature) {
        this.x = x;
        this.y = y;
        this.lookaheadDistance = lookaheadDistance;
        this.velocity = velocity;
        this.curvature = curvature;
    }

    public PathPoint(PathPoint pathPoint) {
        this.x = pathPoint.getX();
        this.y = pathPoint.getY();
        this.maxVelocity = pathPoint.getMaxVelocity();
        this.velocity = pathPoint.getVelocity();
        this.curvature = pathPoint.getCurvature();
        this.lookaheadDistance = pathPoint.getLookaheadDistance();
    }

    public Point2d getPoint() {
        return new Point2d(x, y);
    }

    public static PathPoint origin() {
        return new PathPoint(0, 0, 0);
    }

    public static double distance(PathPoint point1, PathPoint point2) {
        return Point2d.distance(point1.getPoint(), point2.getPoint());
    }

    public static double distancePose2d(PathPoint point, Pose2d pose) {
        return Point2d.distance(point.getPoint(), new Point2d(pose));
    }
}

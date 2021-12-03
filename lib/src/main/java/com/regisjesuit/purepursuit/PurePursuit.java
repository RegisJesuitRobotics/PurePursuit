package com.regisjesuit.purepursuit;

import static com.regisjesuit.purepursuit.utils.MathUtils.sqr;

import com.regisjesuit.purepursuit.path.PathPoint;
import com.regisjesuit.purepursuit.path.PurePursuitPath;
import com.regisjesuit.purepursuit.utils.MathUtils;
import com.regisjesuit.purepursuit.utils.Point2d;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;

public class PurePursuit {

    private final PurePursuitPath path;
    private final double lookaheadDistance;
    private final double trackWidthMeters;
    private Pose2d currentPosition;
    private int closestPointIndex = 0;
    private double lookaheadFractionalIndex = 0;
    private final Point2d lookaheadPoint = new Point2d(0, 0);
    private double lookaheadCurvature = 0;

    /**
     * @param path              The path to follow
     * @param lookaheadDistance the lookahead distance (m)
     * @param trackWidthMeters  trackWidth of the robot in meters
     */
    public PurePursuit(PurePursuitPath path, double lookaheadDistance, double trackWidthMeters) {
        this.path = path;
        this.lookaheadDistance = lookaheadDistance;
        this.trackWidthMeters = trackWidthMeters;

        SmartDashboard.putNumber("Length", path.getSize());
    }

    private void updateClosestPointIndex() {
        List<PathPoint> points = path.getPoints();
        double closestDistance = Double.POSITIVE_INFINITY;
        for (int i = closestPointIndex; i < points.size(); i++) {
            double distance = PathPoint.distancePose2d(points.get(i), currentPosition);
            if (distance <= closestDistance) {
                closestDistance = distance;
                closestPointIndex = i;
            }
        }
        SmartDashboard.putNumber("Closest point", closestPointIndex);
    }

    private void updateLookaheadIndex() {
        List<PathPoint> points = path.getPoints();
        Point2d currentPositionPoint = new Point2d(currentPosition.getX(), currentPosition.getY());
        for (int i = (int) lookaheadFractionalIndex; i < points.size() - 1; i++) {
            PathPoint currentPoint = points.get(i);
            PathPoint nextPoint = points.get(i + 1);
            double intersection = MathUtils.circleIntersectionWithLine(currentPoint.getPoint(), nextPoint.getPoint(),
                currentPositionPoint, lookaheadDistance);

            if (intersection >= 0) {
                // We don't want to go backwards
                if (i + intersection < lookaheadFractionalIndex) {
                    continue;
                }
                lookaheadFractionalIndex = i + intersection;
                double xDistance = nextPoint.getX() - currentPoint.getX();
                double yDistance = nextPoint.getY() - currentPoint.getY();

                lookaheadPoint.x = currentPoint.getX() + xDistance * intersection;
                lookaheadPoint.y = currentPoint.getY() + yDistance * intersection;
                break;
            }
        }
        SmartDashboard.putNumber("Lookahead Index", lookaheadFractionalIndex);
    }

    private void updateLookaheadCurvature() {
        double a = Math.tan(-currentPosition.getRotation().getRadians());
        double b = 1;
        double c = a * currentPosition.getX() - currentPosition.getY();

        double x = Math.abs((a * lookaheadPoint.x + b * lookaheadPoint.y + c) / MathUtils.distanceFormula(a, b));
        lookaheadCurvature = 2 * x / sqr(lookaheadDistance);
        lookaheadCurvature *= Math.signum(Math.sin(-currentPosition.getRotation().getRadians())
            * (lookaheadPoint.x - currentPosition.getX())
            - Math.cos(-currentPosition.getRotation().getRadians()) * (lookaheadPoint.y - currentPosition.getY()));
        SmartDashboard.putNumber("Lookahead curvature", lookaheadCurvature);
    }

    public DifferentialDriveWheelSpeeds calculate(Pose2d currentPosition) {
        this.currentPosition = currentPosition;

        updateClosestPointIndex();
        updateLookaheadIndex();
        updateLookaheadCurvature();

        double pointTargetVelocity = path.getPoints().get(closestPointIndex).getVelocity();
        SmartDashboard.putNumber("Point Target Velocity", pointTargetVelocity);

        double leftSpeed = pointTargetVelocity * (2 - lookaheadCurvature * trackWidthMeters) / 2;
        double rightSpeed = pointTargetVelocity * (2 + lookaheadCurvature * trackWidthMeters) / 2;

        return new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed);
    }


    public boolean isDone() {
        return closestPointIndex == path.getSize() - 1;
    }

}


package com.regisjesuit.purepursuit;

import static com.regisjesuit.purepursuit.utils.MathUtils.sqr;

import com.regisjesuit.purepursuit.path.PathPoint;
import com.regisjesuit.purepursuit.path.PurePursuitPath;
import com.regisjesuit.purepursuit.utils.MathUtils;
import com.regisjesuit.purepursuit.utils.Point2d;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;

public class PurePursuit implements Sendable {

    private final PurePursuitPath path;
    private final double trackWidthMeters;
    private final Point2d lookaheadPoint = new Point2d(0, 0);
    private Pose2d currentPosition = new Pose2d();
    private int closestPointIndex = 0;
    private double lookaheadFractionalIndex = 0;
    private double lookaheadCurvature = 0;

    /**
     * @param path              The path to follow
     * @param trackWidthMeters  trackWidth of the robot in meters
     */
    public PurePursuit(PurePursuitPath path, double trackWidthMeters) {
        this.path = path;
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
    }

    private void updateLookaheadIndex() {
        List<PathPoint> points = path.getPoints();
        Point2d currentPositionPoint = new Point2d(currentPosition.getX(), currentPosition.getY());
        for (int i = (int) lookaheadFractionalIndex; i < points.size() - 1; i++) {
            PathPoint currentPoint = points.get(i);
            PathPoint nextPoint = points.get(i + 1);
            double intersection = MathUtils.circleIntersectionWithLine(currentPoint.getPoint(), nextPoint.getPoint(),
                    currentPositionPoint, points.get(closestPointIndex).getLookaheadDistance());

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
    }

    private void updateLookaheadCurvature() {
        double aValue = -Math.tan(currentPosition.getRotation().getRadians());
        double bValue = 1;
        double cValue = Math.tan(currentPosition.getRotation().getRadians()) * currentPosition.getX()
                - currentPosition.getY();

        double xValue = Math.abs(aValue * lookaheadPoint.x + bValue * lookaheadPoint.y + cValue)
                / Math.sqrt(sqr(aValue) + sqr(bValue));
        double side = Math.signum(Math.sin(currentPosition.getRotation().getRadians())
                * (lookaheadPoint.x - currentPosition.getX())
                - Math.cos(currentPosition.getRotation().getRadians()) * (lookaheadPoint.y - currentPosition.getY()));

        this.lookaheadCurvature = (2 * xValue * side) / sqr(path.getPoints().get(closestPointIndex).getLookaheadDistance());
    }

    public DifferentialDriveWheelSpeeds calculate(Pose2d currentPosition) {
        this.currentPosition = currentPosition;

        updateClosestPointIndex();
        updateLookaheadIndex();
        updateLookaheadCurvature();

        double pointTargetVelocity = path.getPoints().get(closestPointIndex).getVelocity();

        double leftSpeed = pointTargetVelocity * (2 + lookaheadCurvature * trackWidthMeters) / 2;
        double rightSpeed = pointTargetVelocity * (2 - lookaheadCurvature * trackWidthMeters) / 2;

        return new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed);
    }


    public boolean isDone() {
        return closestPointIndex == path.getSize() - 1;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("PurePursuit");
        List<PathPoint> points = path.getPoints();
        double[] xValues = new double[points.size()];
        double[] yValues = new double[points.size()];

        for (int i = 0; i < points.size(); i++) {
            xValues[i] = points.get(i).getX();
            yValues[i] = points.get(i).getY();
        }

        builder.addDoubleArrayProperty("xValues", () -> xValues, null);
        builder.addDoubleArrayProperty("yValues", () -> yValues, null);
        builder.addDoubleProperty("robotX", () -> currentPosition.getX(), null);
        builder.addDoubleProperty("robotY", () -> currentPosition.getY(), null);
        builder.addDoubleProperty("lookaheadDistance", () -> points.get(closestPointIndex).getLookaheadDistance(), null);
        builder.addDoubleProperty("lookaheadCurvature", () -> lookaheadCurvature, null);
        builder.addDoubleProperty("lookaheadX", () -> lookaheadPoint.x, null);
        builder.addDoubleProperty("lookaheadY", () -> lookaheadPoint.y, null);

    }
}

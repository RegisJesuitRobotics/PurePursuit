package com.regisjesuit.simulator.kinematics;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;

public class DifferentialDriveSimulator {

    private final double trackWidth;
    private final double maxVelocityChange;
    private double robotX = 0;
    private double robotY = 0;
    private double heading = 0;
    private double lastLeftVelocity = 0;
    private double lastRightVelocity = 0;

    /**
     *
     * @param trackWidth The distance between left wheel and right wheel (m)
     * @param maxVelocityChange The maximum velocity change (m/s)
     */
    public DifferentialDriveSimulator(double trackWidth, double maxVelocityChange) {
        this.trackWidth = trackWidth;
        this.maxVelocityChange = maxVelocityChange;
    }

    /**
     * Updates the simulation. Call every 0.02 seconds
     *
     * @param leftVelocity  left rate (m/s)
     * @param rightVelocity right rate (m/s)
     */
    public void update(double leftVelocity, double rightVelocity) {
        update(leftVelocity, rightVelocity, 0.02);
    }

    public void update(double leftVelocity, double rightVelocity, double interval) {
        leftVelocity = rateLimit(lastLeftVelocity, leftVelocity, interval);
        rightVelocity = rateLimit(lastRightVelocity, rightVelocity, interval);

        double headingChange = (rightVelocity - leftVelocity) / trackWidth;

        robotX += (rightVelocity + leftVelocity) / 2 * Math.cos(headingChange) * interval;
        robotY += (rightVelocity + leftVelocity) / 2 * Math.sin(headingChange) * interval;

        this.heading += headingChange;
        this.lastLeftVelocity = leftVelocity;
        this.lastRightVelocity = rightVelocity;
    }

    public Pose2d getPose() {
        return new Pose2d(robotX, robotY, new Rotation2d(heading));
    }

    private double rateLimit(double lastVelocity, double currentVelocity, double interval) {
        if (Math.abs(currentVelocity - lastVelocity) > (maxVelocityChange * interval)) {
            return lastVelocity + Math.signum(currentVelocity - lastVelocity) * maxVelocityChange;
        }
        return currentVelocity;
    }
}

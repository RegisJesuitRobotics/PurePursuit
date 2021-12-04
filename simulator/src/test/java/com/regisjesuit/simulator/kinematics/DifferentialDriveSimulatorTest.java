package com.regisjesuit.simulator.kinematics;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import org.junit.jupiter.api.Test;

class DifferentialDriveSimulatorTest {
    @Test
    void straightLine() {
        DifferentialDriveSimulator simulator = new DifferentialDriveSimulator(1, 50);
        simulator.update(1, 1);

        Pose2d pose = simulator.getPose();

        assertEquals(0.02, pose.getX(), 0.01);
        assertEquals(0, pose.getY(), 0.01);
        assertEquals(0, pose.getRotation().getRadians());
    }

    @Test
    void straightLineRateLimit() {
        DifferentialDriveSimulator simulator = new DifferentialDriveSimulator(1, 1);
        simulator.update(1, 1, 1);
        Pose2d pose = simulator.getPose();

        assertEquals(1, pose.getX(), 0.01);
        assertEquals(0, pose.getY(), 0.01);
        assertEquals(0, pose.getRotation().getRadians());
    }

    @Test
    void turnInPlace() {
        DifferentialDriveSimulator simulator = new DifferentialDriveSimulator(1, 50);
        simulator.update(0, 1, 1);

        Pose2d pose = simulator.getPose();

        assertEquals(1, pose.getRotation().getRadians());
    }
}

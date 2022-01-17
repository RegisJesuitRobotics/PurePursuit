package com.regisjesuit.purepursuit.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.regisjesuit.purepursuit.path.PathPoint;
import org.junit.jupiter.api.Test;

class MathUtilsTest {
    @Test
    void curvature_ReturnsCorrect() {
        PathPoint curr = new PathPoint(2, 4, 1);
        PathPoint prev = PathPoint.origin();
        PathPoint next = new PathPoint(5, 5, 1);

        assertEquals(0.2, MathUtils.getCurvature(curr, prev, next), 0.0001);
    }

    @Test
    void curvature_StraightLine_ReturnsCorrect() {
        PathPoint curr = new PathPoint(1, 0, 1);
        PathPoint prev = PathPoint.origin();
        PathPoint next = new PathPoint(2, 0, 1);

        assertEquals(0, MathUtils.getCurvature(curr, prev, next), 0.0001);
    }

    @Test
    void vectorAngle_ReturnsCorrect() {
        assertEquals(0.5 * Math.PI, MathUtils.vectorAngle(new Vector2d(0, 1), new Vector2d(1, 0)));
        assertEquals(0.0555, MathUtils.vectorAngle(new Vector2d(3, 4), new Vector2d(2, 3)), 0.001);
        assertEquals(1.7895, MathUtils.vectorAngle(new Vector2d(2, -6), new Vector2d(5, 3)), 0.001);
    }

    @Test
    void vectorAngle_Zero_ReturnsCorrect() {
        assertEquals(0, MathUtils.vectorAngle(new Vector2d(0.0001, 0), new Vector2d(1, 0)));
    }

    @Test
    void circleIntersection_DoesIntersect_ReturnsCorrect() {
        assertEquals(0.5,
                MathUtils.circleIntersectionWithLine(new Point2d(1, 2), new Point2d(3, 2), new Point2d(3, 2), 1));
        assertEquals(0.5,
                MathUtils.circleIntersectionWithLine(new Point2d(3, 2), new Point2d(1, 2), new Point2d(3, 2), 1));
    }

    @Test
    void circleIntersection_DoesNotIntersect_ReturnsCorrect() {
        assertEquals(-1,
                MathUtils.circleIntersectionWithLine(new Point2d(0, 2), new Point2d(1, 2), new Point2d(3, 2), 1));
        assertEquals(-1,
                MathUtils.circleIntersectionWithLine(new Point2d(5, 2), new Point2d(6, 2), new Point2d(3, 2), 1));
    }

    @Test
    void distanceFormula_ThreeFourTriangle_ReturnsCorrect() {
        assertEquals(5, MathUtils.distanceFormula(3, 4));
    }

    @Test
    void distanceFormula_NotNiceTriangle_ReturnsCorrect() {
        assertEquals(3.162, MathUtils.distanceFormula(3, 1), 0.001);
    }

    @Test
    void distanceTo_ThreeFourTriangleNotOrigin_ReturnsCorrect() {
        PathPoint origin = new PathPoint(1, 2, 1);
        PathPoint point = new PathPoint(4, 6, 1);

        assertEquals(5, PathPoint.distance(origin, point), 0);
        assertEquals(5, PathPoint.distance(point, origin), 0);
    }

    @Test
    void distanceTo_ThreeFourTriangleFullyNegative_ReturnsCorrect() {
        PathPoint origin = new PathPoint(-1, -2, 1);
        PathPoint point = new PathPoint(-4, -6, 1);

        assertEquals(5, PathPoint.distance(origin, point), 0);
        assertEquals(5, PathPoint.distance(point, origin), 0);
    }

    @Test
    void distanceTo_ThreeFourTriangleHalfNegative_ReturnsCorrect() {
        PathPoint origin = new PathPoint(-1, -2, 1);
        PathPoint point = new PathPoint(2, 2, 1);

        assertEquals(5, PathPoint.distance(origin, point), 0);
        assertEquals(5, PathPoint.distance(point, origin), 0);
    }
}

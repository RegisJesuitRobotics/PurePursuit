package com.regisjesuit.purepursuit.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Point2dTest {
    @Test
    void distance_ReturnsCorrect() {
        Point2d point1 = new Point2d(1, 1);
        Point2d point2 = new Point2d(4, 5);

        assertEquals(5, Point2d.distance(point1, point2));
        assertEquals(5, Point2d.distance(point2, point1));
    }
}

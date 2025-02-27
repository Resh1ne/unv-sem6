package com.example.algorithms.lb3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HermiteCurve {
    public static List<Point> drawHermiteCurve(Point p0, Point p1, Point t0, Point t1, int steps) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double h1 = 2 * Math.pow(t, 3) - 3 * Math.pow(t, 2) + 1;
            double h2 = -2 * Math.pow(t, 3) + 3 * Math.pow(t, 2);
            double h3 = Math.pow(t, 3) - 2 * Math.pow(t, 2) + t;
            double h4 = Math.pow(t, 3) - Math.pow(t, 2);

            int x = (int) (h1 * p0.x + h2 * p1.x + h3 * t0.x + h4 * t1.x);
            int y = (int) (h1 * p0.y + h2 * p1.y + h3 * t0.y + h4 * t1.y);
            points.add(new Point(x, y));
        }
        return points;
    }
}
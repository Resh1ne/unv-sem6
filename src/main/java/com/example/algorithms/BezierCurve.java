package com.example.algorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    public static List<Point> drawBezierCurve(Point p0, Point p1, Point p2, Point p3, int steps) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double u = 1 - t;
            double tt = t * t;
            double uu = u * u;
            double uuu = uu * u;
            double ttt = tt * t;

            int x = (int) (uuu * p0.x + 3 * uu * t * p1.x + 3 * u * tt * p2.x + ttt * p3.x);
            int y = (int) (uuu * p0.y + 3 * uu * t * p1.y + 3 * u * tt * p2.y + ttt * p3.y);
            points.add(new Point(x, y));
        }
        return points;
    }
}
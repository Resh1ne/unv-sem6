package com.example.algorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BSpline {
    public static List<Point> drawBSpline(List<Point> controlPoints, int steps) {
        List<Point> points = new ArrayList<>();
        int n = controlPoints.size() - 1;
        for (int i = 0; i <= n - 3; i++) {
            for (int j = 0; j <= steps; j++) {
                double t = (double) j / steps;
                double b0 = (1 - t) * (1 - t) * (1 - t) / 6;
                double b1 = (3 * t * t * t - 6 * t * t + 4) / 6;
                double b2 = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
                double b3 = t * t * t / 6;

                int x = (int) (b0 * controlPoints.get(i).x + b1 * controlPoints.get(i + 1).x +
                        b2 * controlPoints.get(i + 2).x + b3 * controlPoints.get(i + 3).x);
                int y = (int) (b0 * controlPoints.get(i).y + b1 * controlPoints.get(i + 1).y +
                        b2 * controlPoints.get(i + 2).y + b3 * controlPoints.get(i + 3).y);
                points.add(new Point(x, y));
            }
        }
        return points;
    }
}
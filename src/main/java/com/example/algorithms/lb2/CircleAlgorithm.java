package com.example.algorithms.lb2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircleAlgorithm {
    public static List<Point> drawCircleBresenham(int xc, int yc, int r) {
        List<Point> points = new ArrayList<>();
        int x = 0, y = r;
        int d = 3 - 2 * r;
        addCirclePoints(points, xc, yc, x, y);

        while (y >= x) {
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
            addCirclePoints(points, xc, yc, x, y);
        }
        return points;
    }

    private static void addCirclePoints(List<Point> points, int xc, int yc, int x, int y) {
        points.add(new Point(xc + x, yc + y));
        points.add(new Point(xc - x, yc + y));
        points.add(new Point(xc + x, yc - y));
        points.add(new Point(xc - x, yc - y));
        points.add(new Point(xc + y, yc + x));
        points.add(new Point(xc - y, yc + x));
        points.add(new Point(xc + y, yc - x));
        points.add(new Point(xc - y, yc - x));
    }
}
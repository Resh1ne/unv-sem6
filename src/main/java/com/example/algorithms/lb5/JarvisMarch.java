package com.example.algorithms.lb5;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JarvisMarch {
    private static Point findMinXPoint(List<Point> points) {
        Point minXPoint = points.get(0);
        for (Point p : points) {
            if (p.x < minXPoint.x || (p.x == minXPoint.x && p.y < minXPoint.y)) {
                minXPoint = p;
            }
        }
        return minXPoint;
    }

    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    public static List<Point> convexHull(List<Point> points) {
        if (points.size() < 3) return points;

        List<Point> hull = new ArrayList<>();

        Point startPoint = findMinXPoint(points);
        Point currentPoint = startPoint;

        do {
            hull.add(currentPoint);
            Point nextPoint = points.get(0);

            for (Point p : points) {
                if (p == currentPoint) continue;
                int orient = orientation(currentPoint, nextPoint, p);
                if (orient == 2 || (orient == 0 && distanceSq(currentPoint, p) > distanceSq(currentPoint, nextPoint))) {
                    nextPoint = p;
                }
            }
            currentPoint = nextPoint;
        } while (currentPoint != startPoint);

        return hull;
    }

    private static int distanceSq(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }
}
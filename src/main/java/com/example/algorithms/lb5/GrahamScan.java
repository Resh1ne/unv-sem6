package com.example.algorithms.lb5;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GrahamScan {
    private static Point findMinYPoint(List<Point> points) {
        Point minYPoint = points.get(0);
        for (Point p : points) {
            if (p.y < minYPoint.y || (p.y == minYPoint.y && p.x < minYPoint.x)) {
                minYPoint = p;
            }
        }
        return minYPoint;
    }

    private static double polarAngle(Point p0, Point p1) {
        return Math.atan2(p1.y - p0.y, p1.x - p0.x);
    }

    private static int distanceSq(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    public static List<Point> convexHull(List<Point> points) {
        if (points.size() < 3) return points;

        Point minYPoint = findMinYPoint(points);

        points.sort((p1, p2) -> {
            double angle1 = polarAngle(minYPoint, p1);
            double angle2 = polarAngle(minYPoint, p2);
            if (angle1 < angle2) return -1;
            if (angle1 > angle2) return 1;
            return Integer.compare(distanceSq(minYPoint, p1), distanceSq(minYPoint, p2));
        });

        Stack<Point> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        for (int i = 2; i < points.size(); i++) {
            while (hull.size() > 1 && orientation(hull.get(hull.size() - 2), hull.peek(), points.get(i)) != 2) {
                hull.pop();
            }
            hull.push(points.get(i));
        }

        return new ArrayList<>(hull);
    }
}
package com.example.algorithms.lb5;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class JarvisMarch {

    // Находим точку с минимальной x-координатой (и минимальной y, если таких несколько)
    private static Point findMinXPoint(List<Point> points) {
        Point minXPoint = points.get(0);
        for (Point p : points) {
            if (p.x < minXPoint.x || (p.x == minXPoint.x && p.y < minXPoint.y)) {
                minXPoint = p;
            }
        }
        return minXPoint;
    }

    // Определяем ориентацию тройки точек (p, q, r)
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0; // Коллинеарны
        return (val > 0) ? 1 : 2; // По часовой или против часовой стрелки
    }

    // Реализация алгоритма Джарвиса
    public static List<Point> convexHull(List<Point> points) {
        if (points.size() < 3) return points;

        List<Point> hull = new ArrayList<>();

        // Находим самую левую точку
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

    // Вычисляем квадрат расстояния между двумя точками
    private static int distanceSq(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }
}
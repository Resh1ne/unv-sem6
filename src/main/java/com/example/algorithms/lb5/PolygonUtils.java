package com.example.algorithms.lb5;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonUtils {
    public static boolean isConvex(List<Point> polygon) {
        if (polygon.size() < 3) return false;

        int n = polygon.size();
        int sign = 0;

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);
            Point p3 = polygon.get((i + 2) % n);

            int crossProduct = (p2.x - p1.x) * (p3.y - p2.y) - (p2.y - p1.y) * (p3.x - p2.x);

            if (crossProduct == 0) continue;

            if (sign == 0) {
                sign = crossProduct > 0 ? 1 : -1;
            } else if (sign * crossProduct < 0) {
                return false;
            }
        }

        return true;
    }

    public static List<Point> calculateInnerNormals(List<Point> polygon) {
        List<Point> normals = new ArrayList<>();
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);

            int dx = p2.x - p1.x;
            int dy = p2.y - p1.y;

            Point normal = new Point(-dy, dx);

            normals.add(normal);
        }

        return normals;
    }

    public static Point findIntersection(Point p1, Point p2, Point p3, Point p4) {
        double x1 = p1.x, y1 = p1.y;
        double x2 = p2.x, y2 = p2.y;
        double x3 = p3.x, y3 = p3.y;
        double x4 = p4.x, y4 = p4.y;

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        if (denominator == 0) {
            System.out.println("Отрезки параллельны или совпадают: " + p1 + "->" + p2 + " и " + p3 + "->" + p4);
            return null;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denominator;

        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            int x = (int) (x1 + t * (x2 - x1));
            int y = (int) (y1 + t * (y2 - y1));
            Point intersection = new Point(x, y);
            System.out.println("Найдено пересечение: " + intersection + " между " + p1 + "->" + p2 + " и " + p3 + "->" + p4);
            return intersection;
        }

        System.out.println("Отрезки не пересекаются: " + p1 + "->" + p2 + " и " + p3 + "->" + p4);
        return null;
    }

    public static List<Point> findIntersectionsWithPolygon(List<Point> line, List<Point> polygon) {
        List<Point> intersections = new ArrayList<>();

        if (line.size() < 2) {
            System.out.println("Линия содержит меньше двух точек: " + line);
            return intersections;
        }

        Point lineStart = line.get(0);
        Point lineEnd = line.get(1);

        System.out.println("Поиск пересечений для линии: " + lineStart + " -> " + lineEnd);

        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            System.out.println("Проверка стороны полигона: " + p1 + " -> " + p2);

            Point intersection = findIntersection(lineStart, lineEnd, p1, p2);
            if (intersection != null) {
                intersections.add(intersection);
            }
        }

        return intersections;
    }
}
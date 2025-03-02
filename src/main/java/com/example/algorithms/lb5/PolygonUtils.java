package com.example.algorithms.lb5;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class PolygonUtils {

    // Метод для проверки выпуклости полигона
    public static boolean isConvex(List<Point> polygon) {
        if (polygon.size() < 3) return false; // Полигон должен иметь хотя бы 3 точки

        int n = polygon.size();
        int sign = 0;

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);
            Point p3 = polygon.get((i + 2) % n);

            // Векторное произведение (p2 - p1) x (p3 - p2)
            int crossProduct = (p2.x - p1.x) * (p3.y - p2.y) - (p2.y - p1.y) * (p3.x - p2.x);

            if (crossProduct == 0) continue; // Коллинеарные точки

            if (sign == 0) {
                sign = crossProduct > 0 ? 1 : -1; // Определяем начальный знак
            } else if (sign * crossProduct < 0) {
                return false; // Знак изменился => полигон невыпуклый
            }
        }

        return true; // Все углы имеют одинаковый знак => полигон выпуклый
    }

    public static List<Point> calculateInnerNormals(List<Point> polygon) {
        List<Point> normals = new ArrayList<>();
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);

            // Вектор стороны полигона
            int dx = p2.x - p1.x;
            int dy = p2.y - p1.y;

            // Внутренняя нормаль (перпендикулярный вектор, направленный внутрь)
            Point normal = new Point(-dy, dx); // Поворот на 90 градусов влево

            normals.add(normal);
        }

        return normals;
    }
}
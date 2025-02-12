package com.example.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BresenhamAlgorithm {

    /**
     * Алгоритм Брезенхема для построения отрезка.
     *
     * @param x1 Начальная координата X.
     * @param y1 Начальная координата Y.
     * @param x2 Конечная координата X.
     * @param y2 Конечная координата Y.
     * @return Список точек, составляющих отрезок.
     */
    public static List<Point> drawLineBresenham(int x1, int y1, int x2, int y2) {
        List<Point> points = new ArrayList<>();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            points.add(new Point(x1, y1));
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }

        return points;
    }
}
package com.example.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DDAAlgorithm {

    /**
     * Алгоритм ЦДА для построения отрезка.
     *
     * @param x1 Начальная координата X.
     * @param y1 Начальная координата Y.
     * @param x2 Конечная координата X.
     * @param y2 Конечная координата Y.
     * @return Список точек, составляющих отрезок.
     */
    public static List<Point> drawLineDDA(int x1, int y1, int x2, int y2) {
        List<Point> points = new ArrayList<>();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy)); // Количество шагов

        float xIncrement = (float) dx / steps; // Приращение по X
        float yIncrement = (float) dy / steps; // Приращение по Y

        float x = x1;
        float y = y1;

        // Добавляем начальную точку
        points.add(new Point(Math.round(x), Math.round(y)));

        for (int i = 0; i < steps; i++) {
            x += xIncrement;
            y += yIncrement;
            // Добавляем промежуточные точки
            points.add(new Point(Math.round(x), Math.round(y)));
        }

        // Добавляем конечную точку
        points.add(new Point(x2, y2));

        return points;
    }
}
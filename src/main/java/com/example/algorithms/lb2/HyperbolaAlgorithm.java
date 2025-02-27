package com.example.algorithms.lb2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class HyperbolaAlgorithm {
    public static List<Point> drawHyperbola(int xc, int yc, int a, int b) {
        List<Point> points = new ArrayList<>();

        int x = a, y = 0;
        int a2 = a * a, b2 = b * b;
        int fx = 2 * b2 * x, fy = 2 * a2 * y;
        int p = b2 - a2 * b + (a2 / 4);

        int limitX = 2 * a; // Ограничение на X для выхода из цикла
        int maxPoints = 10000;

        while (fx > fy && points.size() < maxPoints) {
            addSymmetricPoints(points, xc, yc, x, y);
            y++;
            fy += 2 * a2;

            if (p < 0) {
                p += b2 + fy;
            } else {
                x++;
                fx += 2 * b2;
                p += b2 + fy - fx;
            }

            if (x > limitX) break;
        }

        p = (int) (b2 * (x + 0.5) * (x + 0.5) + a2 * (y + 1) * (y + 1) - a2 * b2);
        while (x <= limitX && points.size() < maxPoints) {
            addSymmetricPoints(points, xc, yc, x, y);
            x++;
            fx += 2 * b2;

            if (p >= 0) {
                p += a2 - fx;
            } else {
                y++;
                fy += 2 * a2;
                p += a2 - fx + fy;
            }

            if (y > limitX) break;
        }

        return points;
    }

    private static void addSymmetricPoints(List<Point> points, int xc, int yc, int x, int y) {
        if (points.size() >= 10000) return;

        points.add(new Point(xc + x, yc + y));
        points.add(new Point(xc - x, yc + y));
        points.add(new Point(xc + x, yc - y));
        points.add(new Point(xc - x, yc - y));
    }
}
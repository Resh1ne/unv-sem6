package com.example.algorithms.lb2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class EllipseAlgorithm {
    public static List<Point> drawEllipseBresenham(int xc, int yc, int rx, int ry) {
        List<Point> points = new ArrayList<>();

        int x = 0, y = ry;
        int rxSq = rx * rx;
        int rySq = ry * ry;
        int twoRxSq = 2 * rxSq;
        int twoRySq = 2 * rySq;
        int p;
        int px = 0;
        int py = twoRxSq * y;

        // Первая область
        p = (int) (rySq - (rxSq * ry) + (0.25 * rxSq));
        while (px < py) {
            points.add(new Point(xc + x, yc + y));
            points.add(new Point(xc - x, yc + y));
            points.add(new Point(xc + x, yc - y));
            points.add(new Point(xc - x, yc - y));

            x++;
            px += twoRySq;
            if (p < 0) {
                p += rySq + px;
            } else {
                y--;
                py -= twoRxSq;
                p += rySq + px - py;
            }
        }

        // Вторая область
        p = (int) (rySq * (x + 0.5) * (x + 0.5) + rxSq * (y - 1) * (y - 1) - rxSq * rySq);
        while (y >= 0) {
            points.add(new Point(xc + x, yc + y));
            points.add(new Point(xc - x, yc + y));
            points.add(new Point(xc + x, yc - y));
            points.add(new Point(xc - x, yc - y));

            y--;
            py -= twoRxSq;
            if (p > 0) {
                p += rxSq - py;
            } else {
                x++;
                px += twoRySq;
                p += rxSq - py + px;
            }
        }

        return points;
    }
}
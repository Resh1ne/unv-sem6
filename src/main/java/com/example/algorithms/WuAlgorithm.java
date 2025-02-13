package com.example.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class WuAlgorithm {

    /**
     * Алгоритм Ву для построения сглаженного отрезка.
     *
     * @param x0 Начальная координата X.
     * @param y0 Начальная координата Y.
     * @param x1 Конечная координата X.
     * @param y1 Конечная координата Y.
     * @return Список точек, составляющих отрезок.
     */
    public static List<Point> drawLineWu(int x0, int y0, int x1, int y1) {
        List<Point> points = new ArrayList<>();

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            // Если отрезок крутой, меняем местами x и y
            int temp = x0;
            x0 = y0;
            y0 = temp;

            temp = x1;
            x1 = y1;
            y1 = temp;
        }
        if (x0 > x1) {
            // Если отрезок рисуется справа налево, меняем местами начальную и конечную точки
            int temp = x0;
            x0 = x1;
            x1 = temp;

            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        float dx = x1 - x0;
        float dy = y1 - y0;
        float gradient = dy / dx;

        // Первая точка
        float xend = Math.round(x0);
        float yend = y0 + gradient * (xend - x0);
        float xgap = rfpart(x0 + 0.5f);
        int xpxl1 = (int) xend;
        int ypxl1 = ipart(yend);
        if (steep) {
            points.add(new Point(ypxl1, xpxl1));
            points.add(new Point(ypxl1 + 1, xpxl1));
        } else {
            points.add(new Point(xpxl1, ypxl1));
            points.add(new Point(xpxl1, ypxl1 + 1));
        }
        float intery = yend + gradient;

        // Вторая точка
        xend = Math.round(x1);
        yend = y1 + gradient * (xend - x1);
        xgap = fpart(x1 + 0.5f);
        int xpxl2 = (int) xend;
        int ypxl2 = ipart(yend);
        if (steep) {
            points.add(new Point(ypxl2, xpxl2));
            points.add(new Point(ypxl2 + 1, xpxl2));
        } else {
            points.add(new Point(xpxl2, ypxl2));
            points.add(new Point(xpxl2, ypxl2 + 1));
        }

        // Основной цикл
        if (steep) {
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                points.add(new Point(ipart(intery), x));
                points.add(new Point(ipart(intery) + 1, x));
                intery += gradient;
            }
        } else {
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                points.add(new Point(x, ipart(intery)));
                points.add(new Point(x, ipart(intery) + 1));
                intery += gradient;
            }
        }

        return points;
    }

    /**
     * Возвращает целую часть числа.
     *
     * @param x Число.
     * @return Целая часть числа.
     */
    private static int ipart(float x) {
        return (int) x;
    }

    /**
     * Возвращает дробную часть числа.
     *
     * @param x Число.
     * @return Дробная часть числа.
     */
    private static float fpart(float x) {
        return x - ipart(x);
    }

    /**
     * Возвращает 1 минус дробная часть числа.
     *
     * @param x Число.
     * @return 1 минус дробная часть числа.
     */
    private static float rfpart(float x) {
        return 1 - fpart(x);
    }
}
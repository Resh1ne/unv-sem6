# Графический интерфейс интеллектуальных систем. Графический редактор
## Цель
Разработать элементарный графический редактор, реализующий построение отрезков с помощью алгоритма ЦДА, целочисленного алгоритма Брезенхема и алгоритма Ву. Вызов способа генерации отрезка задается из пункта меню и доступно через панель инструментов «Отрезки». В редакторе кроме режима генерации отрезков в пользовательском окне должен быть предусмотрен отладочный режим, где отображается пошаговое решение на дискретной сетке.
## Описание алгоритмов
### Цифровой Дифференциальный Анализатор
Цифровой дифференциальный анализатор (ЦДА) – это алгоритм, основанный на аппроксимации прямой линии путем равномерного приращения координат. Он разбивает отрезок на равные шаги по одной из координат и вычисляет соответствующие значения другой координаты.
### Алгоритм Брезенхема
Алгоритм Брезенхема основан на выборе оптимального пикселя с использованием целочисленных вычислений. В отличие от ЦДА, он исключает необходимость работы с дробными числами, используя пошаговое накопление ошибки, чтобы принять решение о том, какой пиксель закрасить на следующем шаге.
### Алгоритм Ву
Алгоритм Ву предназначен для построения сглаженных (антиалиасинговых) линий. В отличие от Брезенхема, который выбирает один пиксель на каждом шаге, Ву использует два соседних пикселя, назначая им разные уровни яркости, чтобы сгладить границы линии и уменьшить эффект "ступенек" (aliasing).
## Интерфейс
![image](https://github.com/user-attachments/assets/57c62740-7d48-491c-84de-790e2cd63263)

![image](https://github.com/user-attachments/assets/51156386-b6fd-44a3-b794-e85cf05d5de7)

## Реализация
### Цифровой Дифференциальный Анализатор
```
package com.example.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DDAAlgorithm {
    public static List<Point> drawLineDDA(int x1, int y1, int x2, int y2) {
        List<Point> points = new ArrayList<>();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = x1;
        float y = y1;

        points.add(new Point(Math.round(x), Math.round(y)));

        for (int i = 0; i < steps; i++) {
            x += xIncrement;
            y += yIncrement;
            points.add(new Point(Math.round(x), Math.round(y)));
        }

        points.add(new Point(x2, y2));

        return points;
    }
}
```
### Алгоритм Брезенхема
```
package com.example.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BresenhamAlgorithm {
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
```
### Алгоритм Ву
```
package com.example.algorithms;

import java.util.ArrayList;
import java.util.List;

public class WuAlgorithm {
    public static List<Pixel> drawLineWu(int x0, int y0, int x1, int y1) {
        List<Pixel> pixels = new ArrayList<>();

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            int temp = x0;
            x0 = y0;
            y0 = temp;

            temp = x1;
            x1 = y1;
            y1 = temp;
        }

        boolean reverse = x0 > x1;
        if (reverse) {
            int temp = x0;
            x0 = x1;
            x1 = temp;

            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        float dx = x1 - x0;
        float dy = y1 - y0;
        float gradient = dx == 0 ? 1 : dy / dx;

        float xend = Math.round(x0);
        float yend = y0 + gradient * (xend - x0);
        float xgap = rfpart(x0 + 0.5f);
        int xpxl1 = (int) xend;
        int ypxl1 = ipart(yend);

        if (steep) {
            plot(pixels, ypxl1, xpxl1, rfpart(yend) * xgap);
            plot(pixels, ypxl1 + 1, xpxl1, fpart(yend) * xgap);
        } else {
            plot(pixels, xpxl1, ypxl1, rfpart(yend) * xgap);
            plot(pixels, xpxl1, ypxl1 + 1, fpart(yend) * xgap);
        }

        float intery = yend + gradient;

        xend = Math.round(x1);
        yend = y1 + gradient * (xend - x1);
        xgap = fpart(x1 + 0.5f);
        int xpxl2 = (int) xend;
        int ypxl2 = ipart(yend);

        if (steep) {
            plot(pixels, ypxl2, xpxl2, rfpart(yend) * xgap);
            plot(pixels, ypxl2 + 1, xpxl2, fpart(yend) * xgap);
        } else {
            plot(pixels, xpxl2, ypxl2, rfpart(yend) * xgap);
            plot(pixels, xpxl2, ypxl2 + 1, fpart(yend) * xgap);
        }

        if (steep) {
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                plot(pixels, ipart(intery), x, rfpart(intery));
                plot(pixels, ipart(intery) + 1, x, fpart(intery));
                intery += gradient;
            }
        } else {
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                plot(pixels, x, ipart(intery), rfpart(intery));
                plot(pixels, x, ipart(intery) + 1, fpart(intery));
                intery += gradient;
            }
        }

        if (reverse) {
            reverseList(pixels);
        }

        return pixels;
    }

    private static void plot(List<Pixel> pixels, int x, int y, float brightness) {
        pixels.add(new Pixel(x, y, brightness));
    }

    private static int ipart(float x) {
        return (int) x;
    }

    private static float fpart(float x) {
        return x - ipart(x);
    }

    private static float rfpart(float x) {
        return 1 - fpart(x);
    }

    private static void reverseList(List<Pixel> pixels) {
        int i = 0, j = pixels.size() - 1;
        while (i < j) {
            Pixel temp = pixels.get(i);
            pixels.set(i, pixels.get(j));
            pixels.set(j, temp);
            i++;
            j--;
        }
    }
}
```
## Технологии
- Java
- JavaFX
- Maven
## Вывод
В результате реализации графического редактора, использующего алгоритмы построения отрезков (ЦДА, Брезенхема и Ву), была создана система, обеспечивающая интерактивное рисование отрезков с возможностью отображения пошагового процесса.

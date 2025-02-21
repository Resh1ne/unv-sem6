# Лабораторная работа №2
## Цель
Разработать элементарный графический редактор, реализующий построение линий второго порядка. Вызов способа генерации линии второго порядка задается из пункта меню и доступно через панель инструментов «Линии 2-го порядка». В редакторе кроме режима генерации линий второго порядка в пользовательском окне должен быть предусмотрен отладочный режим, где отображается пошаговое решение на дискретной сетке.
## Описание алгоритмов
### Алгоритм для окружности
Алгоритм **Брезенхэма** для окружности основан на построении пикселей по восьмисимметрии. Вместо вычисления уравнения окружности, он использует целочисленные вычисления и пошаговое принятие решений.
### Алгоритм для элипса
Алгоритм **Брезенхэма** для эллипса — это целочисленный алгоритм растеризации эллипса. Он использует **инкрементальный метод** и основан на уравнении эллипса. Так как эллипс симметричен относительно обеих осей, достаточно вычислить точки только в одной четверти, а затем отразить их по симметрии.
### Алгоритм для гиперболы
Этот алгоритм реализует метод **Брезенхема** для рисования гиперболы. Он основан на пошаговом приближении гиперболы за счет целочисленных вычислений, что делает его быстрым и эффективным.
### Алгоритм для параболы
Этот алгоритм реализует **метод Брезенхема** для отрисовки **параболы**. Он использует дискретные (целочисленные) вычисления, что делает его быстрым и эффективным для растровой графики.
## Интерфейс
![image](https://github.com/user-attachments/assets/57c62740-7d48-491c-84de-790e2cd63263)

![image](https://github.com/user-attachments/assets/51156386-b6fd-44a3-b794-e85cf05d5de7)

## Реализация
### Алгоритм окружности
```
public class CircleAlgorithm {
    public static List<Point> drawCircleBresenham(int xc, int yc, int r) {
        List<Point> points = new ArrayList<>();
        int x = 0, y = r;
        int d = 3 - 2 * r;
        addCirclePoints(points, xc, yc, x, y);

        while (y >= x) {
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
            addCirclePoints(points, xc, yc, x, y);
        }
        return points;
    }

    private static void addCirclePoints(List<Point> points, int xc, int yc, int x, int y) {
        points.add(new Point(xc + x, yc + y));
        points.add(new Point(xc - x, yc + y));
        points.add(new Point(xc + x, yc - y));
        points.add(new Point(xc - x, yc - y));
        points.add(new Point(xc + y, yc + x));
        points.add(new Point(xc - y, yc + x));
        points.add(new Point(xc + y, yc - x));
        points.add(new Point(xc - y, yc - x));
    }
}
```
### Алгоритм элипса
```
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
```
### Алгоритм гиперболы
```
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
```
### Алгоритм параболы
```
public class ParabolaAlgorithm {

    public static List<Point> drawParabola(int x0, int y0, int a) {
        List<Point> points = new ArrayList<>();
        int signA = Integer.signum(a);
        a = Math.abs(a);

        int x = 0;
        int y = 0;
        int p = 1 - 2 * a;

        while (y <= 500) {
            points.add(new Point(x0 + x * signA, y0 + y));
            points.add(new Point(x0 + x * signA, y0 - y));

            if (p < 0) {
                p += 2 * y + 3;
            } else {
                x++;
                p += 2 * y + 3 - 4 * a;
            }
            y++;
        }

        if (signA < 0) {
            x = 0;
            y = 0;
            p = 1 - 2 * a;

            while (y <= 1000) {
                points.add(new Point(x0 - x, y0 + y));
                points.add(new Point(x0 - x, y0 - y));

                if (p < 0) {
                    p += 2 * y + 3;
                } else {
                    x++;
                    p += 2 * y + 3 - 4 * a;
                }
                y++;
            }
        }

        return points;
    }
}
```
## Технологии
- Java
- JavaFX
- Maven
## Вывод
В результате разработки графического редактора, были добавлены возможности отрисовки таких объектов как: **окружность**, **элипс**, **парабола** и **гипербола**.

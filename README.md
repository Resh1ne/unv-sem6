# Лабораторная работа №5
## Цель
Разработать элементарный графический редактор, реализующий построение полигонов. Реализованная программа должна уметь проверять полигон на выпуклость, находить его внутренние нормали. Программа должна выполнять построение выпуклых оболочек методом обхода Грэхема и методом Джарвиса. Выбор метода задается из пункта меню и должен быть доступен через панель инструментов «Построение полигонов». Графический редактор должен позволять рисовать линии первого порядка (лабораторная работа №1) и определять точки пересечения отрезка со стороной полигона, также программа должна определять принадлежность введенной точки полигону.
## Алгоритмы
Для проверки полигона на выпуклость используется алгоритм, основанный на определении направления поворота для каждой тройки последовательных вершин полигона. Если все тройки вершин имеют одинаковое направление поворота, то полигон является выпуклым.
### Метод обхода Грэхема
Алгоритм, который строит выпуклую оболочку, обходя точки в порядке увеличения угла относительно начальной точки.
### Метод Джарвиса
Алгоритм, который строит выпуклую оболочку, последовательно находя точки с наименьшим углом относительно предыдущей точки.
## Интерфейс
![image](https://github.com/user-attachments/assets/ccf0fe76-0c81-43c6-b2b5-03929ea623fd)

## Реализация
### Метод обхода Грэхема
```
public static List<Point> convexHull(List<Point> points) {
        if (points.size() < 3) return points;

        Point minYPoint = findMinYPoint(points);

        points.sort((p1, p2) -> {
            double angle1 = polarAngle(minYPoint, p1);
            double angle2 = polarAngle(minYPoint, p2);
            if (angle1 < angle2) return -1;
            if (angle1 > angle2) return 1;
            return Integer.compare(distanceSq(minYPoint, p1), distanceSq(minYPoint, p2));
        });

        Stack<Point> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        for (int i = 2; i < points.size(); i++) {
            while (hull.size() > 1 && orientation(hull.get(hull.size() - 2), hull.peek(), points.get(i)) != 2) {
                hull.pop();
            }
            hull.push(points.get(i));
        }

        return new ArrayList<>(hull);
    }
```
### Метод Джарвиса
```
public static List<Point> convexHull(List<Point> points) {
        if (points.size() < 3) return points;

        List<Point> hull = new ArrayList<>();

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
```
### Класс для определения выпуклости и пересечений
```
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
```
## Технологии
- Java
- JavaFX
- Maven
## Вывод
В ходе выполнения лабораторной работы был разработан элементарный графический редактор, который позволяет выполнять различные геометрические преобразования на полигонах. Программа успешно проверяет полигоны на выпуклость, находит внутренние нормали, строит выпуклые оболочки методами Грэхема и Джарвиса, а также определяет точки пересечения отрезков и принадлежность точек полигонам. Реализованные алгоритмы работают корректно и позволяют эффективно решать поставленные задачи.

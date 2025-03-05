# Лабораторная работа №6
## Цель
Целью данной лабораторной работы является разработка элементарного графического редактора, который позволяет выполнять построение полигонов и их заполнение с использованием различных алгоритмов растровой развертки и заполнения с затравкой. Программа должна поддерживать режим отладки для визуализации пошагового выполнения алгоритмов.
## Алгоритмы
### Алгоритм растровой развертки с упорядоченным списком рёбер
Сначала строится список рёбер (Edge Table, ET), отсортированный по y-координате нижнего конца рёбер.

Каждое ребро содержит:
- Верхнюю и нижнюю y-координаты,
- x-координату начальной точки,
- Обратную величину наклона (dx/dy).
- 
Далее выполняется проход построчно, начиная от минимального y к максимальному:
1. Добавляются рёбра из списка, если их нижний конец достигнут.
2. Удаляются рёбра, если их верхний конец достигнут.
3. Сортируется текущий список активных рёбер.
4. Выполняется заливка между парами пересечений.
5. x-координаты рёбер обновляются (x += dx/dy).
### Алгоритм растровой развертки с использованием списка активных рёбер
Вместо хранения всех рёбер сразу, ведётся только список активных рёбер.

Алгоритм работы:
1. Рёбра, начинающиеся на текущей строке, добавляются в AET.
2. Все рёбра, у которых ymax совпадает с текущим y, удаляются.
3. В AET рёбра сортируются по x-координате.
4. Выполняется заливка между парами пересечений.
5. Обновляются x-координаты активных рёбер (x += dx/dy).
6. Повторяется, пока не будет обработан весь многоугольник.
Этот метод динамически обновляет список активных рёбер, что делает его эффективным.
### Простой алгоритм заполнения с затравкой
Применяется для заливки замкнутых областей.

Выбирается затравочная точка внутри области. Затем рекурсивно или с помощью стека проверяются соседние пиксели:
- Если они имеют исходный цвет, то перекрашиваются в новый.
- Для каждого изменённого пикселя проверяются его соседи.
- Алгоритм продолжается, пока вся область не будет закрашена.
  
Недостатки:
- Рекурсивный вариант может привести к переполнению стека.
- Медленно работает на сложных формах.
### Построчный алгоритм заполнения с затравкой
Оптимизированная версия Flood Fill, использующая построчную заливку.

Алгоритм работы:
1. Выбирается затравочная точка.
2. Определяется горизонтальный отрезок пикселей в этой строке, который можно закрасить (до границы).
3. Заполняется найденный отрезок.
4. В стек добавляются затравочные точки соседних строк (над и под текущей).
5. Повторяется, пока не будут обработаны все пиксели.

Преимущества:
- Избегает переполнения стека.
- Работает быстрее обычного Flood Fill.
## Интерфейс
![image](https://github.com/user-attachments/assets/db9305bb-5c4d-49f4-815f-d31466de0fe6)

## Реализация
### Алгоритм растровой развертки с упорядоченным списком рёбер
```
public class ScanlineFillAlgorithm {
    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.size() < 3) {
            return filledPixels;
        }

        int minY = polygon.get(0).y;
        int maxY = polygon.get(0).y;
        for (Point p : polygon) {
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y != p2.y) {
                Edge edge = new Edge(p1, p2);
                edges.add(edge);
            }
        }

        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();

            for (Edge edge : edges) {
                if (y >= edge.minY && y < edge.maxY) {
                    int x = (int) (edge.x + (y - edge.y1) * edge.slope);
                    intersections.add(x);
                }
            }

            Collections.sort(intersections);

            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }
        }

        return filledPixels;
    }

    private static class Edge {
        int y1, y2;
        int x;
        double slope;
        int minY, maxY;

        public Edge(Point p1, Point p2) {
            if (p1.y < p2.y) {
                y1 = p1.y;
                y2 = p2.y;
                x = p1.x;
            } else {
                y1 = p2.y;
                y2 = p1.y;
                x = p2.x;
            }
            minY = y1;
            maxY = y2;
            slope = (double) (p2.x - p1.x) / (p2.y - p1.y);
        }
    }
}
```
### Алгоритм растровой развертки с использованием списка активных рёбер
```
public class ScanlineFillWithAELAlgorithm {

    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.isEmpty()) {
            return filledPixels;
        }

        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (Point p : polygon) {
            if (p.y < yMin) yMin = p.y;
            if (p.y > yMax) yMax = p.y;
        }

        Map<Integer, List<Edge>> edgeTable = new HashMap<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y == p2.y) continue;

            Edge edge = new Edge(Math.max(p1.y, p2.y), p1.y < p2.y ? p1.x : p2.x, (float) (p2.x - p1.x) / (p2.y - p1.y));
            int yStart = Math.min(p1.y, p2.y);

            if (!edgeTable.containsKey(yStart)) {
                edgeTable.put(yStart, new ArrayList<>());
            }
            edgeTable.get(yStart).add(edge);
        }

        List<Edge> activeEdges = new ArrayList<>();

        for (int y = yMin; y <= yMax; y++) {
            if (edgeTable.containsKey(y)) {
                activeEdges.addAll(edgeTable.get(y));
            }

            int finalY = y;
            activeEdges.removeIf(edge -> edge.yMax <= finalY);

            activeEdges.sort(Comparator.comparing(edge -> edge.x));

            for (int i = 0; i < activeEdges.size(); i += 2) {
                int xStart = (int) Math.ceil(activeEdges.get(i).x);
                int xEnd = (int) Math.floor(activeEdges.get(i + 1).x);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }

            for (Edge edge : activeEdges) {
                edge.x += edge.slope;
            }
        }

        return filledPixels;
    }
}
```
### Простой алгоритм заполнения с затравкой
```
public class FloodFillAlgorithm {

    public static List<Point> fillPolygon(List<Point> polygon, Point seed) {
        List<Point> filledPixels = new ArrayList<>();
        if (polygon.isEmpty() || seed == null) {
            return filledPixels;
        }

        int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
        for (Point p : polygon) {
            if (p.x < xMin) xMin = p.x;
            if (p.x > xMax) xMax = p.x;
            if (p.y < yMin) yMin = p.y;
            if (p.y > yMax) yMax = p.y;
        }

        boolean[][] visited = new boolean[yMax - yMin + 1][xMax - xMin + 1];

        Stack<Point> stack = new Stack<>();
        stack.push(seed);

        while (!stack.isEmpty()) {
            Point current = stack.pop();
            int x = current.x;
            int y = current.y;

            if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && !visited[y - yMin][x - xMin]) {
                if (isPointInsidePolygon(polygon, current)) {
                    visited[y - yMin][x - xMin] = true;
                    filledPixels.add(new Point(x, y));

                    stack.push(new Point(x + 1, y));
                    stack.push(new Point(x - 1, y));
                    stack.push(new Point(x, y + 1));
                    stack.push(new Point(x, y - 1));
                }
            }
        }

        return filledPixels;
    }

    private static boolean isPointInsidePolygon(List<Point> polygon, Point point) {
        int intersections = 0;
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);

            if (p1.y == p2.y) continue;

            if (point.y > Math.min(p1.y, p2.y) && point.y <= Math.max(p1.y, p2.y)) {
                double xIntersection = (double) ((point.y - p1.y) * (p2.x - p1.x)) / (p2.y - p1.y) + p1.x;

                if (p1.x == p2.x || point.x <= xIntersection) {
                    intersections++;
                }
            }
        }

        return intersections % 2 != 0;
    }
}
```
### Построчный алгоритм заполнения с затравкой
```
public class ScanlineFillAlgorithm {
    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.size() < 3) {
            return filledPixels;
        }

        int minY = polygon.get(0).y;
        int maxY = polygon.get(0).y;
        for (Point p : polygon) {
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y != p2.y) {
                Edge edge = new Edge(p1, p2);
                edges.add(edge);
            }
        }

        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();

            for (Edge edge : edges) {
                if (y >= edge.minY && y < edge.maxY) {
                    int x = (int) (edge.x + (y - edge.y1) * edge.slope);
                    intersections.add(x);
                }
            }

            Collections.sort(intersections);

            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }
        }

        return filledPixels;
    }

    private static class Edge {
        int y1, y2;
        int x;
        double slope;
        int minY, maxY;

        public Edge(Point p1, Point p2) {
            if (p1.y < p2.y) {
                y1 = p1.y;
                y2 = p2.y;
                x = p1.x;
            } else {
                y1 = p2.y;
                y2 = p1.y;
                x = p2.x;
            }
            minY = y1;
            maxY = y2;
            slope = (double) (p2.x - p1.x) / (p2.y - p1.y);
        }
    }
}
```
## Технологии
- Java
- JavaFX
- Maven
## Вывод
В ходе выполнения лабораторной работы был разработан элементарный графический редактор, который позволяет выполнять построение полигонов и их заполнение с использованием различных алгоритмов растровой развертки и заполнения с затравкой. Программа поддерживает режим отладки, что позволяет визуализировать пошаговое выполнение алгоритмов. Реализованные алгоритмы работают корректно и позволяют эффективно решать поставленные задачи.

# Лабораторная работа №7
## Цель
Целью данной лабораторной работы является разработка графической программы, которая выполняет триангуляцию Делоне и строит диаграмму Вороного по заданному набору точек.
## Алгоритмы
### Триангуляция Делоне
Триангуляция Делоне — это разбиение множества точек на плоскости на треугольники таким образом, что ни одна точка не попадает внутрь описанной окружности любого треугольника. Это обеспечивает максимальную равномерность треугольников и минимизирует "острые" углы.
### Диаграмма Вороного
Диаграмма Вороного — это разбиение плоскости на области (ячейки), где каждая ячейка соответствует одной точке из заданного множества. Все точки внутри ячейки ближе к соответствующей точке, чем к любой другой точке из множества.
## Интерфейс
![image](https://github.com/user-attachments/assets/a2581ade-bdfd-462c-9ac9-c5cf197ab86e)

![image](https://github.com/user-attachments/assets/6ddf2086-ae0b-4b58-88ed-4ec4efe3e432)

## Реализация
### Триангуляция Делоне
```
public class Triangulation {
    private List<Triangle> triangles;

    public Triangulation(List<Pixel> points) {
        triangles = new ArrayList<>();
        performTriangulation(points);
    }

    private void performTriangulation(List<Pixel> points) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Pixel p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }

        int dx = maxX - minX;
        int dy = maxY - minY;
        int deltaMax = Math.max(dx, dy) * 10;

        Pixel p1 = new Pixel(minX - deltaMax, minY - deltaMax);
        Pixel p2 = new Pixel(minX + deltaMax, minY - deltaMax);
        Pixel p3 = new Pixel(minX, minY + deltaMax * 2);

        Triangle superTriangle = new Triangle(p1, p2, p3);
        triangles.add(superTriangle);

        for (Pixel p : points) {
            List<Triangle> badTriangles = new ArrayList<>();
            List<Edge> edges = new ArrayList<>();

            for (Triangle t : triangles) {
                if (t.containsInCircumcircle(p)) {
                    badTriangles.add(t);
                    edges.addAll(t.getEdges());
                }
            }

            triangles.removeAll(badTriangles);

            edges = removeDuplicateEdges(edges);

            for (Edge edge : edges) {
                triangles.add(new Triangle(edge.getA(), edge.getB(), p));
            }
        }

        triangles.removeIf(t -> t.hasVertex(p1) || t.hasVertex(p2) || t.hasVertex(p3));
    }

    private List<Edge> removeDuplicateEdges(List<Edge> edges) {
        Map<Edge, Integer> edgeCount = new HashMap<>();

        for (Edge edge : edges) {
            edgeCount.put(edge, edgeCount.getOrDefault(edge, 0) + 1);
        }

        List<Edge> uniqueEdges = new ArrayList<>();
        for (Map.Entry<Edge, Integer> entry : edgeCount.entrySet()) {
            if (entry.getValue() == 1) {
                uniqueEdges.add(entry.getKey());
            }
        }

        return uniqueEdges;
    }

}
```
### Диаграмма Вороного
```
public class VoronoiDiagram {
    public List<LineSegment> getVoronoiEdges(List<Triangle> triangles, Rectangle boundingBox) {
        Map<Edge, List<Triangle>> edgeTriangleMap = new HashMap<>();
        for (Triangle t : triangles) {
            for (Edge edge : t.getEdges()) {
                edgeTriangleMap.computeIfAbsent(edge, k -> new ArrayList<>()).add(t);
            }
        }

        List<LineSegment> voronoiEdges = new ArrayList<>();

        for (Map.Entry<Edge, List<Triangle>> entry : edgeTriangleMap.entrySet()) {
            List<Triangle> adjacentTriangles = entry.getValue();
            if (adjacentTriangles.size() == 2) {
                Pixel cc1 = adjacentTriangles.get(0).getCircumcenter();
                Pixel cc2 = adjacentTriangles.get(1).getCircumcenter();
                voronoiEdges.add(new LineSegment(cc1, cc2));
            } else if (adjacentTriangles.size() == 1) {
                Triangle t = adjacentTriangles.get(0);
                Pixel cc = t.getCircumcenter();

                Pixel p1 = entry.getKey().getA();
                Pixel p2 = entry.getKey().getB();

                double ex = p2.getX() - p1.getX();
                double ey = p2.getY() - p1.getY();

                double cand1X = -ey;
                double cand2Y = -ex;

                Pixel p3 = t.getThirdVertex(entry.getKey());
                double dot1 = cand1X * (p3.getX() - cc.getX()) + ex * (p3.getY() - cc.getY());
                double dot2 = ey * (p3.getX() - cc.getX()) + cand2Y * (p3.getY() - cc.getY());
                double chosenDx, chosenDy;
                if (dot1 < dot2) {
                    chosenDx = cand1X;
                    chosenDy = ex;
                } else {
                    chosenDx = ey;
                    chosenDy = cand2Y;
                }
                double len = sqrt(chosenDx * chosenDx + chosenDy * chosenDy);
                if (len != 0) {
                    chosenDx /= len;
                    chosenDy /= len;
                }
                Pixel ccExtended = intersectRayWithRectangle(cc, chosenDx, chosenDy, boundingBox);
                voronoiEdges.add(new LineSegment(cc, ccExtended));
            }
        }
        return voronoiEdges;
    }

    private Pixel intersectRayWithRectangle(Pixel origin, double dx, double dy, Rectangle rect) {
        double xMin = rect.getX();
        double yMin = rect.getY();
        double xMax = rect.getX() + rect.getWidth();
        double yMax = rect.getY() + rect.getHeight();
        double tMin = Double.MAX_VALUE;

        if (dx != 0) {
            double t1 = (xMin - origin.getX()) / dx;
            double t2 = (xMax - origin.getX()) / dx;
            if (t1 > 0) tMin = min(tMin, t1);
            if (t2 > 0) tMin = min(tMin, t2);
        }
        if (dy != 0) {
            double t3 = (yMin - origin.getY()) / dy;
            double t4 = (yMax - origin.getY()) / dy;
            if (t3 > 0) tMin = min(tMin, t3);
            if (t4 > 0) tMin = min(tMin, t4);
        }
        if (tMin == Double.MAX_VALUE) {
            return origin;
        }
        int ix = (int) Math.round(origin.getX() + dx * tMin);
        int iy = (int) Math.round(origin.getY() + dy * tMin);
        return new Pixel(ix, iy);
    }
}
```
## Технологии
- Java
- JavaFX
- Maven
## Вывод
В ходе выполнения лабораторной работы была разработана программа, которая успешно выполняет триангуляцию Делоне и строит диаграмму Вороного для заданного набора точек. Это позволило на практике изучить и применить методы вычислительной геометрии, что является важным навыком для решения задач, связанных с анализом и визуализацией данных.

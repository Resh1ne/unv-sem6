# Лабораторная работа №3
## Цель
Разработать элементарный графический редактор, реализующий построение параметрических кривых, используя форму Эрмита, форму Безье и B-сплайн.
## Описание алгоритмов
### Кривая Эрмита 
Метод построения кривых, использующий начальные и конечные точки, а также касательные в этих точках.
### Кривая Безье 
Параметрическая кривая, определяемая опорными точками, с использованием полиномиальных функций.
### B-сплайн
Гибкий метод построения кривых, который позволяет более плавно контролировать форму кривой за счет весовых коэффициентов.
## Интерфейс
![image](https://github.com/user-attachments/assets/c48c6a69-0677-4879-b4da-b52841d0a78f)


## Реализация
### Кривая Эрмита
```
public class HermiteCurve {
    public static List<Point> drawHermiteCurve(Point p0, Point p1, Point t0, Point t1, int steps) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double h1 = 2 * Math.pow(t, 3) - 3 * Math.pow(t, 2) + 1;
            double h2 = -2 * Math.pow(t, 3) + 3 * Math.pow(t, 2);
            double h3 = Math.pow(t, 3) - 2 * Math.pow(t, 2) + t;
            double h4 = Math.pow(t, 3) - Math.pow(t, 2);

            int x = (int) (h1 * p0.x + h2 * p1.x + h3 * t0.x + h4 * t1.x);
            int y = (int) (h1 * p0.y + h2 * p1.y + h3 * t0.y + h4 * t1.y);
            points.add(new Point(x, y));
        }
        return points;
    }
}
```
### Кривая Безье
```
public class BezierCurve {
    public static List<Point> drawBezierCurve(Point p0, Point p1, Point p2, Point p3, int steps) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double u = 1 - t;
            double tt = t * t;
            double uu = u * u;
            double uuu = uu * u;
            double ttt = tt * t;

            int x = (int) (uuu * p0.x + 3 * uu * t * p1.x + 3 * u * tt * p2.x + ttt * p3.x);
            int y = (int) (uuu * p0.y + 3 * uu * t * p1.y + 3 * u * tt * p2.y + ttt * p3.y);
            points.add(new Point(x, y));
        }
        return points;
    }
}
```
### B-сплайн
```
public class BSpline {
    public static List<Point> drawBSpline(List<Point> controlPoints, int steps) {
        List<Point> points = new ArrayList<>();
        int n = controlPoints.size() - 1;
        for (int i = 0; i <= n - 3; i++) {
            for (int j = 0; j <= steps; j++) {
                double t = (double) j / steps;
                double b0 = (1 - t) * (1 - t) * (1 - t) / 6;
                double b1 = (3 * t * t * t - 6 * t * t + 4) / 6;
                double b2 = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
                double b3 = t * t * t / 6;

                int x = (int) (b0 * controlPoints.get(i).x + b1 * controlPoints.get(i + 1).x +
                        b2 * controlPoints.get(i + 2).x + b3 * controlPoints.get(i + 3).x);
                int y = (int) (b0 * controlPoints.get(i).y + b1 * controlPoints.get(i + 1).y +
                        b2 * controlPoints.get(i + 2).y + b3 * controlPoints.get(i + 3).y);
                points.add(new Point(x, y));
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
Разработанный графический редактор успешно реализует построение параметрических кривых Эрмита, Безье и B-сплайнов. Добавлена возможность корректировки опорных точек и состыковки сегментов. Реализованы базовые функции матричных вычислений для работы с кривыми.

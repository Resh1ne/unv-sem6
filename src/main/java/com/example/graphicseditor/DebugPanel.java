package com.example.graphicseditor;

import com.example.algorithms.DDAAlgorithm;
import com.example.algorithms.BresenhamAlgorithm;
import com.example.algorithms.WuAlgorithm;
import com.example.algorithms.CircleAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DebugPanel extends JPanel {
    private final List<Point> debugPoints = new ArrayList<>();
    private Point startPoint = null; // Начальная точка
    private ShapeType shapeType = ShapeType.LINE; // Выбранная фигура
    private AlgorithmType algorithmType = AlgorithmType.DDA; // Выбранный алгоритм
    private static final int SCALE = 10; // Размер ячейки сетки
    private static final int DELAY = 20; // Задержка анимации

    public DebugPanel() {
        setBackground(Color.BLACK); // Чёрный фон для контраста

        // Обработка кликов мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (startPoint == null) {
                    startPoint = snapToGrid(e.getPoint()); // Привязываем к сетке
                } else {
                    Point endPoint = snapToGrid(e.getPoint());
                    drawShape(startPoint, endPoint);
                    startPoint = null; // Сбрасываем начальную точку
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        drawGrid(g2); // Рисуем сетку

        // Рисуем точки белым цветом
        g2.setColor(Color.WHITE);
        for (Point p : debugPoints) {
            g2.fillRect(p.x * SCALE, p.y * SCALE, SCALE, SCALE);
        }
    }

    /**
     * Рисует сетку пикселей.
     */
    private void drawGrid(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();
        g2.setColor(Color.DARK_GRAY);

        for (int x = 0; x < width; x += SCALE) {
            g2.drawLine(x, 0, x, height);
        }
        for (int y = 0; y < height; y += SCALE) {
            g2.drawLine(0, y, width, y);
        }
    }

    /**
     * Привязывает координаты к ближайшей точке сетки.
     */
    private Point snapToGrid(Point p) {
        return new Point(p.x / SCALE, p.y / SCALE);
    }

    /**
     * Рисует фигуру в зависимости от выбора.
     */
    private void drawShape(Point start, Point end) {
        debugPoints.clear();
        repaint();

        switch (shapeType) {
            case LINE:
                drawLine(start, end);
                break;
            case CIRCLE:
                drawCircle(start, end);
                break;
        }
    }

    /**
     * Рисует линию выбранным алгоритмом.
     */
    private void drawLine(Point start, Point end) {
        List<Point> points;
        switch (algorithmType) {
            case DDA:
                points = DDAAlgorithm.drawLineDDA(start.x, start.y, end.x, end.y);
                break;
            case BRESENHAM:
                points = BresenhamAlgorithm.drawLineBresenham(start.x, start.y, end.x, end.y);
                break;
            case WU:
                points = WuAlgorithm.drawLineWu(start.x, start.y, end.x, end.y);
                break;
            default:
                points = new ArrayList<>();
        }

        animateDrawing(points);
    }

    /**
     * Рисует окружность, используя алгоритм Брезенхема.
     */
    private void drawCircle(Point center, Point edge) {
        int radius = (int) Math.sqrt(Math.pow(edge.x - center.x, 2) + Math.pow(edge.y - center.y, 2));
        List<Point> points = CircleAlgorithm.drawCircleBresenham(center.x, center.y, radius);

        animateDrawing(points);
    }

    /**
     * Анимированное добавление точек.
     */
    private void animateDrawing(List<Point> points) {
        new Thread(() -> {
            for (Point p : points) {
                debugPoints.add(p);
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Сеттеры для выбора фигуры и алгоритма
    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
package com.example.graphicseditor;

import com.example.algorithms.lb1.BresenhamAlgorithm;
import com.example.algorithms.lb1.DDAAlgorithm;
import com.example.algorithms.lb1.Pixel;
import com.example.algorithms.lb1.WuAlgorithm;
import com.example.algorithms.lb5.GrahamScan;
import com.example.algorithms.lb5.JarvisMarch;
import com.example.algorithms.lb5.PolygonAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PolygonPanel extends JPanel {
    private final List<List<Point>> allLines = new ArrayList<>(); // Все линии (чёрные)
    private final List<List<Point>> allPolygons = new ArrayList<>(); // Все полигоны (синие)
    private final List<List<Point>> allConvexHulls = new ArrayList<>(); // Все выпуклые оболочки (красные)
    private final List<Point> currentLine = new ArrayList<>();
    private final List<List<Pixel>> allWuLines = new ArrayList<>();
    private final List<Pixel> currentWuLine = new ArrayList<>();
    private final List<Point> polygonPoints = new ArrayList<>(); // Текущие точки полигона
    private Point startPoint = null;
    private ShapeType shapeType = ShapeType.LINE;
    private AlgorithmType algorithmType = AlgorithmType.DDA;

    public PolygonPanel() {
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (shapeType == ShapeType.POLYGON || shapeType == ShapeType.CONVEX_HULL) {
                    if (e.getButton() == MouseEvent.BUTTON3) { // Правая кнопка мыши
                        if (polygonPoints.size() > 2) {
                            if (shapeType == ShapeType.CONVEX_HULL) {
                                // Строим выпуклую оболочку
                                List<Point> convexHullPoints;
                                if (algorithmType == AlgorithmType.GRAHAM) {
                                    convexHullPoints = GrahamScan.convexHull(polygonPoints);
                                } else {
                                    convexHullPoints = JarvisMarch.convexHull(polygonPoints);
                                }
                                allConvexHulls.add(convexHullPoints); // Сохраняем выпуклую оболочку
                            } else {
                                // Рисуем полигон
                                List<Point> polygonPixels = PolygonAlgorithm.drawPolygon(polygonPoints, algorithmType);
                                allPolygons.add(polygonPixels); // Сохраняем полигон
                            }
                            polygonPoints.clear();
                            repaint();
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON1) { // Левая кнопка мыши
                        polygonPoints.add(e.getPoint());
                        repaint();
                    }
                } else {
                    if (startPoint == null) {
                        startPoint = e.getPoint();
                    } else {
                        Point endPoint = e.getPoint();
                        drawShape(startPoint, endPoint);
                        startPoint = null;
                        repaint();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Отрисовка всех линий (чёрные)
        g2d.setColor(Color.BLACK);
        for (List<Point> line : allLines) {
            for (int i = 0; i < line.size() - 1; i++) {
                Point p1 = line.get(i);
                Point p2 = line.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Отрисовка всех полигонов (синие)
        g2d.setColor(Color.BLUE);
        for (List<Point> polygon : allPolygons) {
            for (int i = 0; i < polygon.size() - 1; i++) {
                Point p1 = polygon.get(i);
                Point p2 = polygon.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            // Замыкаем полигон
            Point p1 = polygon.get(polygon.size() - 1);
            Point p2 = polygon.get(0);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Отрисовка всех выпуклых оболочек (красные)
        g2d.setColor(Color.RED);
        for (List<Point> convexHull : allConvexHulls) {
            for (int i = 0; i < convexHull.size() - 1; i++) {
                Point p1 = convexHull.get(i);
                Point p2 = convexHull.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            // Замыкаем оболочку
            Point p1 = convexHull.get(convexHull.size() - 1);
            Point p2 = convexHull.get(0);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Отрисовка текущей линии
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < currentLine.size() - 1; i++) {
            Point p1 = currentLine.get(i);
            Point p2 = currentLine.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Отрисовка линий Ву
        for (List<Pixel> line : allWuLines) {
            for (Pixel pixel : line) {
                float brightness = pixel.getBrightness();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, brightness));
                g2d.fillRect(pixel.getX(), pixel.getY(), 1, 1);
            }
        }

        for (Pixel pixel : currentWuLine) {
            float brightness = pixel.getBrightness();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, brightness));
            g2d.fillRect(pixel.getX(), pixel.getY(), 1, 1);
        }

        // Отрисовка текущего полигона
        if (polygonPoints.size() > 1) {
            g2d.setColor(Color.BLUE); // Цвет для текущего полигона
            for (int i = 0; i < polygonPoints.size() - 1; i++) {
                Point p1 = polygonPoints.get(i);
                Point p2 = polygonPoints.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    private void drawShape(Point start, Point end) {
        if (Objects.requireNonNull(shapeType) == ShapeType.LINE) {
            drawLine(start, end);
        }
    }

    private void drawLine(Point start, Point end) {
        if (algorithmType == AlgorithmType.WU) {
            currentWuLine.clear();
            currentWuLine.addAll(WuAlgorithm.drawLineWu(start.x, start.y, end.x, end.y));
            allWuLines.add(new ArrayList<>(currentWuLine));
        } else {
            currentLine.clear();
            switch (algorithmType) {
                case DDA:
                    currentLine.addAll(DDAAlgorithm.drawLineDDA(start.x, start.y, end.x, end.y));
                    break;
                case BRESENHAM:
                    currentLine.addAll(BresenhamAlgorithm.drawLineBresenham(start.x, start.y, end.x, end.y));
                    break;
            }
            allLines.add(new ArrayList<>(currentLine));
        }
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
        if (shapeType != ShapeType.POLYGON && shapeType != ShapeType.CONVEX_HULL) {
            polygonPoints.clear();
        }
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
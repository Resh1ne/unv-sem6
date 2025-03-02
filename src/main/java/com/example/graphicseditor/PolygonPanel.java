package com.example.graphicseditor;

import com.example.algorithms.lb5.GrahamScan;
import com.example.algorithms.lb5.JarvisMarch;
import com.example.algorithms.lb5.PolygonAlgorithm;
import com.example.algorithms.lb5.PolygonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PolygonPanel extends JPanel {
    private final List<List<Point>> allLines = new ArrayList<>();
    private final List<List<Point>> allPolygons = new ArrayList<>();
    private final List<List<Point>> allConvexHulls = new ArrayList<>();
    private final List<Point> polygonPoints = new ArrayList<>();
    private List<Point> lastCompletedPolygon = new ArrayList<>();
    private Point startPoint = null;
    private ShapeType shapeType = ShapeType.LINE;
    private AlgorithmType algorithmType = AlgorithmType.DDA;

    public PolygonPanel() {
        setBackground(Color.WHITE);

        JButton intersectionButton = new JButton("Найти пересечения");
        intersectionButton.addActionListener(e -> showIntersectionPoints());

        setLayout(new BorderLayout());
        add(intersectionButton, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (shapeType == ShapeType.POLYGON || shapeType == ShapeType.CONVEX_HULL) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        if (polygonPoints.size() > 2) {
                            if (shapeType == ShapeType.CONVEX_HULL) {
                                List<Point> convexHullPoints;
                                if (algorithmType == AlgorithmType.GRAHAM) {
                                    convexHullPoints = GrahamScan.convexHull(polygonPoints);
                                } else {
                                    convexHullPoints = JarvisMarch.convexHull(polygonPoints);
                                }
                                allConvexHulls.add(convexHullPoints);
                            } else {
                                List<Point> polygonPixels = PolygonAlgorithm.drawPolygon(polygonPoints, algorithmType);
                                allPolygons.add(polygonPixels);
                            }
                            lastCompletedPolygon = new ArrayList<>(polygonPoints);
                            polygonPoints.clear();
                            repaint();
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
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

        g2d.setColor(Color.BLACK);
        for (List<Point> line : allLines) {
            for (int i = 0; i < line.size() - 1; i++) {
                Point p1 = line.get(i);
                Point p2 = line.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        g2d.setColor(Color.BLUE);
        for (List<Point> polygon : allPolygons) {
            for (int i = 0; i < polygon.size() - 1; i++) {
                Point p1 = polygon.get(i);
                Point p2 = polygon.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            Point p1 = polygon.get(polygon.size() - 1);
            Point p2 = polygon.get(0);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        g2d.setColor(Color.RED);
        for (List<Point> convexHull : allConvexHulls) {
            for (int i = 0; i < convexHull.size() - 1; i++) {
                Point p1 = convexHull.get(i);
                Point p2 = convexHull.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            Point p1 = convexHull.get(convexHull.size() - 1);
            Point p2 = convexHull.get(0);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        if (polygonPoints.size() > 1) {
            g2d.setColor(Color.GRAY);
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
        List<Point> line = new ArrayList<>();
        line.add(start);
        line.add(end);
        allLines.add(line);
    }

    private void showIntersectionPoints() {
        StringBuilder message = new StringBuilder("Точки пересечения:\n");

        System.out.println("Количество линий: " + allLines.size());
        System.out.println("Количество полигонов: " + allPolygons.size());

        for (List<Point> line : allLines) {
            if (line.size() < 2) {
                System.out.println("Линия пропущена, так как содержит меньше двух точек: " + line);
                continue;
            }

            Point start = line.get(0);
            Point end = line.get(1);

            System.out.println("Обработка линии: " + start + " -> " + end);

            for (List<Point> polygon : allPolygons) {
                System.out.println("Обработка полигона: " + polygon);

                List<Point> intersections = PolygonUtils.findIntersectionsWithPolygon(line, polygon);
                for (Point intersection : intersections) {
                    message.append(String.format("Линия [(%d, %d), (%d, %d)] пересекает полигон в точке (%d, %d)\n",
                            start.x, start.y, end.x, end.y, intersection.x, intersection.y));
                }
            }
        }

        if (message.toString().equals("Точки пересечения:\n")) {
            message.append("Пересечений не найдено.");
        }

        JOptionPane.showMessageDialog(this, message.toString(), "Точки пересечения", JOptionPane.INFORMATION_MESSAGE);
    }

    public List<Point> getLastCompletedPolygonPoints() {
        return lastCompletedPolygon;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
package com.example.graphicseditor;

import com.example.algorithms.DDAAlgorithm;
import com.example.algorithms.BresenhamAlgorithm;
import com.example.algorithms.WuAlgorithm;
import com.example.algorithms.Pixel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {
    private final List<List<Point>> allLines = new ArrayList<>(); // Список всех отрезков (Point)
    private final List<Point> currentLine = new ArrayList<>(); // Текущий отрезок (Point)
    private final List<List<Pixel>> allWuLines = new ArrayList<>(); // Список всех отрезков (Pixel)
    private final List<Pixel> currentWuLine = new ArrayList<>(); // Текущий отрезок (Pixel)
    private Point startPoint = null; // Начальная точка
    private ShapeType shapeType = ShapeType.LINE; // Выбранная фигура
    private AlgorithmType algorithmType = AlgorithmType.DDA; // Выбранный алгоритм

    public EditorPanel() {
        setBackground(Color.WHITE);

        // Обработка событий мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (startPoint == null) {
                    // Если начальная точка не задана, сохраняем её
                    startPoint = e.getPoint();
                } else {
                    // Если начальная точка задана, рисуем фигуру
                    Point endPoint = e.getPoint();
                    drawShape(startPoint, endPoint);
                    startPoint = null; // Сбрасываем начальную точку
                    repaint(); // Перерисовываем панель
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = getGraphics2D((Graphics2D) g);

        // Рисуем все отрезки (Pixel)
        for (List<Pixel> line : allWuLines) {
            for (Pixel pixel : line) {
                float brightness = pixel.getBrightness();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, brightness));
                g2d.fillRect(pixel.getX(), pixel.getY(), 1, 1);
            }
        }

        // Рисуем текущий отрезок (Pixel)
        for (Pixel pixel : currentWuLine) {
            float brightness = pixel.getBrightness();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, brightness));
            g2d.fillRect(pixel.getX(), pixel.getY(), 1, 1);
        }
    }

    private Graphics2D getGraphics2D(Graphics2D g) {
        Graphics2D g2d = g;

        // Рисуем все отрезки (Point)
        for (List<Point> line : allLines) {
            for (int i = 0; i < line.size() - 1; i++) {
                Point p1 = line.get(i);
                Point p2 = line.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Рисуем текущий отрезок (Point)
        for (int i = 0; i < currentLine.size() - 1; i++) {
            Point p1 = currentLine.get(i);
            Point p2 = currentLine.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        return g2d;
    }

    /**
     * Рисует фигуру в зависимости от выбранного типа и алгоритма.
     *
     * @param start Начальная точка.
     * @param end   Конечная точка.
     */
    private void drawShape(Point start, Point end) {
        switch (shapeType) {
            case LINE:
                drawLine(start, end);
                break;
            case CIRCLE:
                // В будущем можно добавить рисование круга
                break;
        }
    }

    /**
     * Рисует отрезок в зависимости от выбранного алгоритма.
     *
     * @param start Начальная точка.
     * @param end   Конечная точка.
     */
    private void drawLine(Point start, Point end) {
        if (algorithmType == AlgorithmType.WU) {
            // Используем Pixel для алгоритма Ву
            currentWuLine.clear();
            currentWuLine.addAll(WuAlgorithm.drawLineWu(start.x, start.y, end.x, end.y));
            allWuLines.add(new ArrayList<>(currentWuLine));
        } else {
            // Используем Point для остальных алгоритмов
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

    // Сеттеры для выбора фигуры и алгоритма
    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
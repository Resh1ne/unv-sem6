package com.example.graphicseditor;

import com.example.algorithms.DDAAlgorithm;
import com.example.algorithms.BresenhamAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {
    private final List<Point> points = new ArrayList<>(); // Список для хранения точек
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
        // Рисуем все точки
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
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
        switch (algorithmType) {
            case DDA:
                points.addAll(DDAAlgorithm.drawLineDDA(start.x, start.y, end.x, end.y));
                break;
            case BRESENHAM:
                points.addAll(BresenhamAlgorithm.drawLineBresenham(start.x, start.y, end.x, end.y));
                break;
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
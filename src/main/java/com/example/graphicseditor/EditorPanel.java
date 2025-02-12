package com.example.graphicseditor;

import com.example.algorithms.DDAAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {
    private final List<Point> lines = new ArrayList<>(); // Список для хранения точек отрезков
    private Point startPoint = null; // Начальная точка отрезка

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
                    // Если начальная точка задана, рисуем отрезок до текущей точки
                    Point endPoint = e.getPoint();
                    List<Point> linePoints = DDAAlgorithm.drawLineDDA(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    lines.addAll(linePoints); // Добавляем все точки отрезка
                    startPoint = null; // Сбрасываем начальную точку
                    repaint(); // Перерисовываем панель
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Рисуем все отрезки
        for (int i = 0; i < lines.size() - 1; i++) {
            Point p1 = lines.get(i);
            Point p2 = lines.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}
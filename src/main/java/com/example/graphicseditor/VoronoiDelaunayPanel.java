package com.example.graphicseditor;

import com.example.algorithms.lb7.LineSegment;
import com.example.algorithms.lb7.Pixel;
import com.example.algorithms.lb7.Rectangle;
import com.example.algorithms.lb7.Triangle;
import com.example.algorithms.lb7.Triangulation;
import com.example.algorithms.lb7.VoronoiDiagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class VoronoiDelaunayPanel extends JPanel {

    private final List<Pixel> points = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();
    private List<LineSegment> voronoiEdges = new ArrayList<>();
    private boolean showDelaunay = true;

    public VoronoiDelaunayPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    points.add(new Pixel(e.getX(), e.getY()));
                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (points.size() >= 3) {
                        Triangulation triangulation = new Triangulation(points);
                        triangles = triangulation.getTriangles();

                        Rectangle boundingBox = new Rectangle(0, 0, getWidth(), getHeight());
                        VoronoiDiagram voronoiDiagram = new VoronoiDiagram();
                        voronoiEdges = voronoiDiagram.getVoronoiEdges(triangles, boundingBox);

                        repaint();
                    }
                }
            }
        });

        JButton toggleButton = getjButton();

        setLayout(new BorderLayout());
        add(toggleButton, BorderLayout.SOUTH);
    }

    private JButton getjButton() {
        JButton toggleButton = new JButton("Переключить на диаграмму Вороного");
        toggleButton.addActionListener(e -> {
            showDelaunay = !showDelaunay; // Переключаем флаг
            if (showDelaunay) {
                toggleButton.setText("Переключить на диаграмму Вороного");
            } else {
                toggleButton.setText("Переключить на триангуляцию Делоне");
            }
            repaint();
        });
        return toggleButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        for (Pixel point : points) {
            g.fillOval(point.getX() - 3, point.getY() - 3, 6, 6);
        }

        if (showDelaunay) {
            g.setColor(Color.RED);
            for (Triangle triangle : triangles) {
                drawTriangle(g, triangle);
            }
        } else {
            g.setColor(Color.GREEN);
            for (LineSegment edge : voronoiEdges) {
                g.drawLine(edge.getStart().getX(), edge.getStart().getY(),
                        edge.getEnd().getX(), edge.getEnd().getY());
            }
        }
    }

    private void drawTriangle(Graphics g, Triangle triangle) {
        Pixel a = triangle.getA();
        Pixel b = triangle.getB();
        Pixel c = triangle.getC();

        g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
        g.drawLine(b.getX(), b.getY(), c.getX(), c.getY());
        g.drawLine(c.getX(), c.getY(), a.getX(), a.getY());
    }
}
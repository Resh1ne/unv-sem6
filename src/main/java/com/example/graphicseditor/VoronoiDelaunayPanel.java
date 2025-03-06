package com.example.graphicseditor;

import com.example.algorithms.lb7.Pixel;
import com.example.algorithms.lb7.Triangle;
import com.example.algorithms.lb7.Triangulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class VoronoiDelaunayPanel extends JPanel {

    private List<Pixel> points = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();

    public VoronoiDelaunayPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    points.add(new Pixel(e.getX(), e.getY()));
                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    Triangulation triangulation = new Triangulation(points);
                    triangles = triangulation.getTriangles();
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        for (Pixel point : points) {
            g.fillOval(point.getX() - 3, point.getY() - 3, 6, 6);
        }

        g.setColor(Color.RED);
        for (Triangle triangle : triangles) {
            drawTriangle(g, triangle);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Voronoi and Delaunay Triangulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new VoronoiDelaunayPanel());
        frame.setVisible(true);
    }
}
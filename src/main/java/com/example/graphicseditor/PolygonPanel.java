package com.example.graphicseditor;

import com.example.algorithms.lb6.ScanlineFillAlgorithm;
import com.example.algorithms.lb6.ScanlineFillWithAELAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PolygonPanel extends JPanel {
    private final List<Point> polygonPoints = new ArrayList<>();
    private final List<List<Point>> allPolygons = new ArrayList<>();
    private final List<Point> filledPixels = new ArrayList<>();
    private final Timer fillTimer;
    private List<Point> pixelsToFill;
    private FillAlgorithmType fillAlgorithmType = FillAlgorithmType.SCANLINE_FILL;
    public PolygonPanel() {
        setBackground(Color.WHITE);

        JButton fillButton = new JButton("Заполнить полигон");
        fillButton.addActionListener(e -> {
            if (!allPolygons.isEmpty()) {
                startFillAnimation();
            } else {
                JOptionPane.showMessageDialog(this, "Сначала создайте полигон!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fillButton);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (polygonPoints.size() > 2) {
                        polygonPoints.add(new Point(polygonPoints.get(0)));
                        allPolygons.add(new ArrayList<>(polygonPoints));
                        polygonPoints.clear();
                        repaint();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    polygonPoints.add(e.getPoint());
                    repaint();
                }
            }
        });

        fillTimer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pixelsToFill.isEmpty()) {
                    int batchSize = 100;
                    for (int i = 0; i < batchSize && !pixelsToFill.isEmpty(); i++) {
                        filledPixels.add(pixelsToFill.remove(0));
                    }
                    repaint();
                } else {
                    fillTimer.stop();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLUE);
        for (List<Point> polygon : allPolygons) {
            drawPolygon(g2d, polygon);
        }

        if (polygonPoints.size() > 1) {
            g2d.setColor(Color.GRAY);
            drawPolygon(g2d, polygonPoints);
        }

        g2d.setColor(Color.GREEN);
        for (Point p : filledPixels) {
            g2d.fillRect(p.x, p.y, 1, 1);
        }
    }

    private void drawPolygon(Graphics2D g2d, List<Point> polygon) {
        for (int i = 0; i < polygon.size() - 1; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        if (polygon.size() > 2) {
            Point p1 = polygon.get(polygon.size() - 1);
            Point p2 = polygon.get(0);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private void startFillAnimation() {
        if (!allPolygons.isEmpty()) {
            List<Point> lastPolygon = allPolygons.get(allPolygons.size() - 1);
            switch (fillAlgorithmType) {
                case SCANLINE_FILL:
                    pixelsToFill = new ArrayList<>(ScanlineFillAlgorithm.fillPolygon(lastPolygon));
                    break;
                case SCANLINE_FILL_WITH_AEL:
                    pixelsToFill = new ArrayList<>(ScanlineFillWithAELAlgorithm.fillPolygon(lastPolygon));
                    break;
            }
            filledPixels.clear();
            fillTimer.start();
        }
    }

    public void setFillAlgorithmType(FillAlgorithmType fillAlgorithmType) {
        this.fillAlgorithmType = fillAlgorithmType;
    }
}
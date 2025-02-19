package com.example.graphicseditor;

import com.example.algorithms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {
    private final List<List<Point>> allLines = new ArrayList<>();
    private final List<Point> currentLine = new ArrayList<>();
    private final List<List<Pixel>> allWuLines = new ArrayList<>();
    private final List<Pixel> currentWuLine = new ArrayList<>();
    private final List<List<Point>> allCircles = new ArrayList<>();
    private final List<Point> currentCircle = new ArrayList<>();
    private final List<List<Point>> allEllipses = new ArrayList<>();
    private final List<Point> currentEllipse = new ArrayList<>();

    private Point startPoint = null;
    private ShapeType shapeType = ShapeType.LINE;
    private AlgorithmType algorithmType = AlgorithmType.DDA;

    public EditorPanel() {
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (shapeType == ShapeType.ELLIPSE) {
                    if (startPoint == null) {
                        startPoint = e.getPoint();
                    } else {
                        showEllipseDialog(startPoint);
                        startPoint = null;
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

        for (int i = 0; i < currentLine.size() - 1; i++) {
            Point p1 = currentLine.get(i);
            Point p2 = currentLine.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }


        g2d.setColor(Color.BLACK);
        for (List<Point> circle : allCircles) {
            for (Point p : circle) {
                g2d.fillRect(p.x, p.y, 1, 1);
            }
        }

        g2d.setColor(Color.BLACK);
        for (List<Point> ellipse : allEllipses) {
            for (Point p : ellipse) {
                g2d.fillRect(p.x, p.y, 1, 1);
            }
        }
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
    }

    private void drawShape(Point start, Point end) {
        switch (shapeType) {
            case LINE:
                drawLine(start, end);
                break;
            case CIRCLE:
                drawCircle(start, end);
                break;
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

    private void drawCircle(Point center, Point edge) {
        currentCircle.clear();
        int radius = (int) Math.sqrt(Math.pow(edge.x - center.x, 2) + Math.pow(edge.y - center.y, 2));
        currentCircle.addAll(CircleAlgorithm.drawCircleBresenham(center.x, center.y, radius));
        allCircles.add(new ArrayList<>(currentCircle));
    }

    private void showEllipseDialog(Point center) {
        JTextField widthField = new JTextField();
        JTextField heightField = new JTextField();
        Object[] message = {
                "Введите ширину (rx):", widthField,
                "Введите высоту (ry):", heightField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Параметры эллипса", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int rx = Integer.parseInt(widthField.getText());
                int ry = Integer.parseInt(heightField.getText());
                drawEllipse(center, rx, ry);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Введите корректные числа!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void drawEllipse(Point center, int rx, int ry) {
        currentEllipse.clear();
        currentEllipse.addAll(EllipseAlgorithm.drawEllipseBresenham(center.x, center.y, rx, ry));
        allEllipses.add(new ArrayList<>(currentEllipse));
        repaint();
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
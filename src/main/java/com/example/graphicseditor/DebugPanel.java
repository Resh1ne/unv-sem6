package com.example.graphicseditor;

import com.example.algorithms.DDAAlgorithm;
import com.example.algorithms.BresenhamAlgorithm;
import com.example.algorithms.WuAlgorithm;
import com.example.algorithms.CircleAlgorithm;
import com.example.algorithms.Pixel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DebugPanel extends JPanel {
    private final List<Point> debugPoints = new ArrayList<>();
    private final List<Pixel> debugPixels = new ArrayList<>();
    private Point startPoint = null;
    private ShapeType shapeType = ShapeType.LINE;
    private AlgorithmType algorithmType = AlgorithmType.DDA;
    private static final int SCALE = 10;
    private static final int DELAY = 20;

    public DebugPanel() {
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (startPoint == null) {
                    startPoint = snapToGrid(e.getPoint());
                } else {
                    Point endPoint = snapToGrid(e.getPoint());
                    drawShape(startPoint, endPoint);
                    startPoint = null;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        drawGrid(g2);

        g2.setColor(Color.WHITE);
        for (Point p : debugPoints) {
            g2.fillRect(p.x * SCALE, p.y * SCALE, SCALE, SCALE);
        }

        for (Pixel pixel : debugPixels) {
            float brightness = pixel.getBrightness();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, brightness));
            g2.fillRect(pixel.getX() * SCALE, pixel.getY() * SCALE, SCALE, SCALE);
        }
    }

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

    private Point snapToGrid(Point p) {
        return new Point(p.x / SCALE, p.y / SCALE);
    }

    private void drawShape(Point start, Point end) {
        debugPoints.clear();
        debugPixels.clear();
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

    private void drawLine(Point start, Point end) {
        if (algorithmType == AlgorithmType.WU) {
            List<Pixel> pixels = WuAlgorithm.drawLineWu(start.x, start.y, end.x, end.y);
            animateDrawing(pixels);
        } else {
            List<Point> points;
            switch (algorithmType) {
                case DDA:
                    points = DDAAlgorithm.drawLineDDA(start.x, start.y, end.x, end.y);
                    break;
                case BRESENHAM:
                    points = BresenhamAlgorithm.drawLineBresenham(start.x, start.y, end.x, end.y);
                    break;
                default:
                    points = new ArrayList<>();
            }
            animateDrawing(points);
        }
    }

    private void drawCircle(Point center, Point edge) {
        int radius = (int) Math.sqrt(Math.pow(edge.x - center.x, 2) + Math.pow(edge.y - center.y, 2));
        List<Point> points = CircleAlgorithm.drawCircleBresenham(center.x, center.y, radius);

        animateDrawing(points);
    }

    private void animateDrawing(List<?> elements) {
        new Thread(() -> {
            for (Object element : elements) {
                if (element instanceof Point) {
                    debugPoints.add((Point) element);
                } else if (element instanceof Pixel) {
                    debugPixels.add((Pixel) element);
                }
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
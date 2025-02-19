package com.example.graphicseditor;

import com.example.algorithms.*;

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
                if (shapeType == ShapeType.ELLIPSE || shapeType == ShapeType.HYPERBOLA) {
                    if (startPoint == null) {
                        startPoint = snapToGrid(e.getPoint());
                    } else {
                        if (shapeType == ShapeType.ELLIPSE) {
                            showEllipseDialog(startPoint);
                        } else {
                            showHyperbolaDialog(startPoint);
                        }
                        startPoint = null;
                    }
                } else {
                    if (startPoint == null) {
                        startPoint = snapToGrid(e.getPoint());
                    } else {
                        Point endPoint = snapToGrid(e.getPoint());
                        drawShape(startPoint, endPoint);
                        startPoint = null;
                    }
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
        debugPoints.clear();
        debugPixels.clear();
        repaint();
        List<Point> points = EllipseAlgorithm.drawEllipseBresenham(center.x, center.y, rx, ry);
        animateDrawing(points);
    }

    private void showHyperbolaDialog(Point center) {
        JTextField aField = new JTextField();
        JTextField bField = new JTextField();
        Object[] message = {
                "Введите параметр a:", aField,
                "Введите параметр b:", bField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Параметры гиперболы", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int a = Integer.parseInt(aField.getText());
                int b = Integer.parseInt(bField.getText());
                drawHyperbola(center, a, b);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Введите корректные числа!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void drawHyperbola(Point center, int a, int b) {
        debugPoints.clear();
        debugPixels.clear();
        repaint();
        List<Point> points = HyperbolaAlgorithm.drawHyperbola(center.x, center.y, a, b);
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
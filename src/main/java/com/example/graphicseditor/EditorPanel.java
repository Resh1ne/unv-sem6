package com.example.graphicseditor;

import com.example.algorithms.BSpline;
import com.example.algorithms.BezierCurve;
import com.example.algorithms.BresenhamAlgorithm;
import com.example.algorithms.CircleAlgorithm;
import com.example.algorithms.DDAAlgorithm;
import com.example.algorithms.EllipseAlgorithm;
import com.example.algorithms.HermiteCurve;
import com.example.algorithms.HyperbolaAlgorithm;
import com.example.algorithms.ParabolaAlgorithm;
import com.example.algorithms.Pixel;
import com.example.algorithms.WuAlgorithm;

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
    private final List<List<Point>> allHyperbolas = new ArrayList<>();
    private final List<Point> currentHyperbola = new ArrayList<>();
    private final List<List<Point>> allParabolas = new ArrayList<>();
    private final List<Point> currentParabola = new ArrayList<>();
    private final List<List<Point>> allHermiteCurves = new ArrayList<>();
    private final List<Point> currentHermiteCurve = new ArrayList<>();
    private final List<List<Point>> allBezierCurves = new ArrayList<>();
    private final List<Point> currentBezierCurve = new ArrayList<>();
    private final List<List<Point>> allBSplines = new ArrayList<>();
    private final List<Point> currentBSpline = new ArrayList<>();
    private final List<Point> controlPoints = new ArrayList<>();

    private Point startPoint = null;
    private ShapeType shapeType = ShapeType.LINE;
    private AlgorithmType algorithmType = AlgorithmType.DDA;

    public EditorPanel() {
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (shapeType == ShapeType.HERMITE || shapeType == ShapeType.BEZIER || shapeType == ShapeType.BSPLINE) {
                        controlPoints.add(e.getPoint());
                        repaint();
                    } else if (shapeType == ShapeType.ELLIPSE || shapeType == ShapeType.HYPERBOLA || shapeType == ShapeType.PARABOLA) {
                        if (startPoint == null) {
                            startPoint = e.getPoint();
                        } else {
                            if (shapeType == ShapeType.ELLIPSE) {
                                showEllipseDialog(startPoint);
                            } else if (shapeType == ShapeType.HYPERBOLA) {
                                showHyperbolaDialog(startPoint);
                            } else if (shapeType == ShapeType.PARABOLA) {
                                showParabolaDialog(startPoint);
                            }
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
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (shapeType == ShapeType.HERMITE || shapeType == ShapeType.BEZIER || shapeType == ShapeType.BSPLINE) {
                        if (controlPoints.size() >= getRequiredControlPoints(shapeType)) {
                            drawShape();
                            controlPoints.clear();
                        } else {
                            JOptionPane.showMessageDialog(EditorPanel.this,
                                    "Недостаточно точек для построения кривой!",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
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

        g2d.setColor(Color.BLACK);
        for (List<Point> hyperbola : allHyperbolas) {
            for (Point p : hyperbola) {
                g2d.fillRect(p.x, p.y, 1, 1);
            }
        }

        g2d.setColor(Color.BLACK);
        for (List<Point> parabola : allParabolas) {
            for (Point p : parabola) {
                g2d.fillRect(p.x, p.y, 1, 1);
            }
        }

        if (shapeType == ShapeType.HERMITE || shapeType == ShapeType.BEZIER || shapeType == ShapeType.BSPLINE) {
            g2d.setColor(Color.RED);
            for (Point p : controlPoints) {
                g2d.fillOval(p.x - 3, p.y - 3, 6, 6);
            }
        }

        g2d.setColor(Color.BLUE);
        for (List<Point> curve : allHermiteCurves) {
            for (Point p : curve) {
                g2d.fillRect(p.x, p.y, 1, 1);
            }
        }

        g2d.setColor(Color.GREEN);
        for (List<Point> curve : allBezierCurves) {
            for (Point p : curve) {
                g2d.fillRect(p.x, p.y, 1, 1);
            }
        }

        g2d.setColor(Color.RED);
        for (List<Point> curve : allBSplines) {
            for (Point p : curve) {
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

    private void drawShape() {
        switch (shapeType) {
            case HERMITE:
                drawHermiteCurve(controlPoints);
                break;
            case BEZIER:
                drawBezierCurve(controlPoints);
                break;
            case BSPLINE:
                drawBSpline(controlPoints);
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
        currentHyperbola.clear();
        currentHyperbola.addAll(HyperbolaAlgorithm.drawHyperbola(center.x, center.y, a, b));
        allHyperbolas.add(new ArrayList<>(currentHyperbola));
        repaint();
    }

    private void showParabolaDialog(Point center) {
        JTextField aField = new JTextField();
        Object[] message = {
                "Введите параметр a:", aField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Параметры параболы", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int a = Integer.parseInt(aField.getText());
                drawParabola(center, a);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Введите корректное число!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void drawParabola(Point center, int a) {
        currentParabola.clear();
        currentParabola.addAll(ParabolaAlgorithm.drawParabola(center.x, center.y, a));
        allParabolas.add(new ArrayList<>(currentParabola));
        repaint();
    }

    private void drawHermiteCurve(List<Point> points) {
        if (points.size() < 4) return;
        Point p0 = points.get(0);
        Point p1 = points.get(1);
        Point t0 = points.get(2);
        Point t1 = points.get(3);
        currentHermiteCurve.clear();
        currentHermiteCurve.addAll(HermiteCurve.drawHermiteCurve(p0, p1, t0, t1, 100));
        allHermiteCurves.add(new ArrayList<>(currentHermiteCurve));
        repaint();
    }

    private void drawBezierCurve(List<Point> points) {
        if (points.size() < 4) return;
        Point p0 = points.get(0);
        Point p1 = points.get(1);
        Point p2 = points.get(2);
        Point p3 = points.get(3);
        currentBezierCurve.clear();
        currentBezierCurve.addAll(BezierCurve.drawBezierCurve(p0, p1, p2, p3, 100));
        allBezierCurves.add(new ArrayList<>(currentBezierCurve));
        repaint();
    }

    private void drawBSpline(List<Point> points) {
        if (points.size() < 4) return;
        currentBSpline.clear();
        currentBSpline.addAll(BSpline.drawBSpline(points, 100));
        allBSplines.add(new ArrayList<>(currentBSpline));
        repaint();
    }

    private int getRequiredControlPoints(ShapeType shapeType) {
        switch (shapeType) {
            case HERMITE:
                return 4; // 2 точки + 2 вектора касательных
            case BEZIER:
                return 4; //4 точки
            case BSPLINE:
                return 4; // минимум 4 точки
            default:
                return 2;
        }
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
        controlPoints.clear();
        repaint();
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
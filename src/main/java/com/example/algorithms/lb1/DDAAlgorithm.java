package com.example.algorithms.lb1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DDAAlgorithm {
    public static List<Point> drawLineDDA(int x1, int y1, int x2, int y2) {
        List<Point> points = new ArrayList<>();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = x1;
        float y = y1;

        points.add(new Point(Math.round(x), Math.round(y)));

        for (int i = 0; i < steps; i++) {
            x += xIncrement;
            y += yIncrement;
            points.add(new Point(Math.round(x), Math.round(y)));
        }

        points.add(new Point(x2, y2));

        return points;
    }
}
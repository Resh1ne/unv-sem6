package com.example.algorithms.lb5;

import com.example.algorithms.lb1.BresenhamAlgorithm;
import com.example.algorithms.lb1.DDAAlgorithm;
import com.example.graphicseditor.AlgorithmType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonAlgorithm {

    public static List<Point> drawPolygon(List<Point> points, AlgorithmType algorithmType) {
        List<Point> polygonPixels = new ArrayList<>();

        if (points.size() < 3) {
            return polygonPixels;
        }

        points.add(points.get(0));

        for (int i = 0; i < points.size() - 1; i++) {
            Point start = points.get(i);
            Point end = points.get(i + 1);

            switch (algorithmType) {
                case DDA:
                    polygonPixels.addAll(DDAAlgorithm.drawLineDDA(start.x, start.y, end.x, end.y));
                    break;
                case BRESENHAM:
                    polygonPixels.addAll(BresenhamAlgorithm.drawLineBresenham(start.x, start.y, end.x, end.y));
                    break;
            }
        }

        return polygonPixels;
    }
}
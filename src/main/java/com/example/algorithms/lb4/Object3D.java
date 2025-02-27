package com.example.algorithms.lb4;

import java.util.ArrayList;
import java.util.List;

public class Object3D {
    private final List<double[]> vertices = new ArrayList<>();
    private final List<int[]> faces = new ArrayList<>();

    public void addVertex(double x, double y, double z) {
        vertices.add(new double[]{x, y, z, 1});
    }

    public void addFace(int[] vertexIndices) {
        faces.add(vertexIndices);
    }

    public List<double[]> getVertices() {
        return vertices;
    }

    public List<int[]> getFaces() {
        return faces;
    }

    public void transform(Matrix4x4 matrix) {
        for (double[] vertex : vertices) {
            double[] transformed = matrix.transform(vertex);
            System.arraycopy(transformed, 0, vertex, 0, 4);
        }
    }
}
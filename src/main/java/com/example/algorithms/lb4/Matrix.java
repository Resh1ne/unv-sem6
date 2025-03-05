package com.example.algorithms.lb4;

public class Matrix {
    private final double[][] matrix = new double[4][4];

    public Matrix() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = (i == j) ? 1 : 0;
            }
        }
    }

    public Matrix multiply(Matrix other) {
        Matrix result = new Matrix();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.matrix[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return result;
    }

    public double[] transform(double[] point) {
        double[] result = new double[4];
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += matrix[i][j] * point[j];
            }
        }
        return result;
    }

    public static Matrix translation(double tx, double ty, double tz) {
        Matrix m = new Matrix();
        m.matrix[0][3] = tx;
        m.matrix[1][3] = ty;
        m.matrix[2][3] = tz;
        return m;
    }

    public static Matrix scaling(double sx, double sy, double sz) {
        Matrix m = new Matrix();
        m.matrix[0][0] = sx;
        m.matrix[1][1] = sy;
        m.matrix[2][2] = sz;
        return m;
    }

    public static Matrix rotationX(double angle) {
        Matrix m = new Matrix();
        double rad = Math.toRadians(angle);
        m.matrix[1][1] = Math.cos(rad);
        m.matrix[1][2] = -Math.sin(rad);
        m.matrix[2][1] = Math.sin(rad);
        m.matrix[2][2] = Math.cos(rad);
        return m;
    }

    public static Matrix rotationY(double angle) {
        Matrix m = new Matrix();
        double rad = Math.toRadians(angle);
        m.matrix[0][0] = Math.cos(rad);
        m.matrix[0][2] = Math.sin(rad);
        m.matrix[2][0] = -Math.sin(rad);
        m.matrix[2][2] = Math.cos(rad);
        return m;
    }

    public static Matrix perspective(double fov, double aspect, double near, double far) {
        Matrix m = new Matrix();
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2);
        m.matrix[0][0] = f / aspect;
        m.matrix[1][1] = f;
        m.matrix[2][2] = (far + near) / (near - far);
        m.matrix[2][3] = (2 * far * near) / (near - far);
        m.matrix[3][2] = -1;
        m.matrix[3][3] = 0;
        return m;
    }
}
//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 7
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код выполняющий вычисление матриц
//28.04.2025

package com.example.lab;

import java.util.Random;

public class MatrixCalculator {
    private final int m, p, q, n, r;
    private final double[][] A;
    private final double[][] B;
    private final double[] E;
    private final double[][] G;
    private final double[][][] F;
    private final double[][][] D;
    private final double[][] C;

    private int callsOfSumm = 0;
    private int callsOfDifference = 0;
    private int callsOfMultiplying = 0;
    private int callsOfCom = 0;

    public MatrixCalculator(int m, int p, int q, int n) {
        this.m = m;
        this.p = p;
        this.q = q;
        this.n = n;
        this.r = p * m * q;

        this.A = new double[p][m];
        this.B = new double[m][q];
        this.E = new double[m];
        this.G = new double[p][q];
        this.C = new double[p][q];
        this.F = new double[p][q][m];
        this.D = new double[p][q][m];

        initializeMatrices();
    }

    private void initializeMatrices() {
        Random random = new Random();

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = random.nextDouble() * 2 - 1;
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < q; j++) {
                B[i][j] = random.nextDouble() * 2 - 1;
            }
        }

        for (int i = 0; i < m; i++) {
            E[i] = random.nextDouble() * 2 - 1;
        }

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                G[i][j] = random.nextDouble() * 2 - 1;
            }
        }
    }

    public void computeAll() {
        computeFMatrix();
        computeDMatrix();
        computeCMatrix();
    }

    private void computeFMatrix() {
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                for (int k = 0; k < m; k++) {
                    Fijk(i, j, k);
                }
            }
        }
    }

    private void Fijk(int i, int j, int k) {
        callsOfMultiplying += 7;
        callsOfDifference += 3;
        callsOfSumm += 2;

        double a = A[i][k];
        double b = B[k][j];

        double aImplB = implication(a, b);
        double bImplA = implication(b, a);

        F[i][j][k] = (aImplB * (2. * E[k] - 1.) * E[k] +
                bImplA * (1. + (4. * aImplB - 2.) * E[k]) * (1. - E[k]));
    }

    private double implication(double x, double y) {
        callsOfCom++;
        callsOfDifference++;
        return Math.max(1 - x, y);
    }

    private void computeDMatrix() {
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                for (int k = 0; k < m; k++) {
                    Dijk(i, j, k);
                }
            }
        }
    }

    private void Dijk(int i, int j, int k) {
        D[i][j][k] = aAndB(i, j, k);
    }

    private double aAndB(int i, int j, int k) {
        callsOfCom++;
        return Math.min(A[i][k], B[k][j]);
    }

    private void computeCMatrix() {
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < q; j++) {
                C[i][j] = cij(i, j);
            }
        }
    }

    private double cij(int i, int j) {
        callsOfMultiplying += 8;
        callsOfDifference += 3;
        callsOfSumm += 2;
        return fFunc(i, j) * (3. * G[i][j] - 2.) * G[i][j] +
                (dFunc(i, j) + (4. * fAndD(i, j) - 3. * dFunc(i, j)) * G[i][j]) * (1. - G[i][j]);
    }

    private double fFunc(int i, int j) {
        double result = 1;
        for (int k = 0; k < m; k++) {
            result *= F[i][j][k];
        }
        callsOfMultiplying += m - 1;
        return result;
    }

    private double dFunc(int i, int j) {
        double result = 1;
        for (int k = 0; k < m; k++) {
            result *= 1 - D[i][j][k];
        }
        callsOfDifference += m + 1;
        callsOfMultiplying += m - 1;
        callsOfDifference++;
        return 1 - result;
    }

    private double fAndD(int i, int j) {
        return fFunc(i, j) * dFunc(i, j);
    }

    public double[][] getA() {
        return A;
    }

    public double[][] getB() {
        return B;
    }

    public double[] getE() {
        return E;
    }

    public double[][] getG() {
        return G;
    }

    public double[][] getC() {
        return C;
    }

    public int getCallsOfSumm() {
        return callsOfSumm;
    }

    public int getCallsOfDifference() {
        return callsOfDifference;
    }

    public int getCallsOfMultiplying() {
        return callsOfMultiplying;
    }

    public int getCallsOfCom() {
        return callsOfCom;
    }

    public int getR() {
        return r;
    }

    public int getM() {
        return m;
    }

    public int getP() {
        return p;
    }

    public int getQ() {
        return q;
    }

    public int getN() {
        return n;
    }
}

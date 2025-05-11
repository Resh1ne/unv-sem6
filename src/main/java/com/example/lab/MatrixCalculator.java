//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 9
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код выполняющий основные вычисления
//28.04.2025
package com.example.lab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixCalculator {
    private List<List<Double>> A = new ArrayList<>();
    private List<List<Double>> B = new ArrayList<>();
    private List<List<Double>> E = new ArrayList<>();
    private List<List<Double>> G = new ArrayList<>();
    private List<List<Double>> C = new ArrayList<>();

    private int Tn = 0;
    private int p, q, m, n;
    private final MatrixOperations operations;

    public MatrixCalculator(MatrixOperations operations) {
        this.operations = operations;
    }

    public void fillMatrix(int m, int p, int q) {
        this.m = m;
        this.p = p;
        this.q = q;

        Random random = new Random();

        A = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                row.add(operations.round(random.nextDouble() * 2 - 1, 3));
            }
            A.add(row);
        }

        B = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < q; j++) {
                row.add(operations.round(random.nextDouble() * 2 - 1, 3));
            }
            B.add(row);
        }

        E = new ArrayList<>();
        List<Double> eRow = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            eRow.add(operations.round(random.nextDouble() * 2 - 1, 3));
        }
        E.add(eRow);

        G = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < q; j++) {
                row.add(operations.round(random.nextDouble() * 2 - 1, 3));
            }
            G.add(row);
        }
    }

    public void findC(int x, int y, int m, int n) {
        this.n = n;
        C = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < y; j++) {
                row.add(findCij(i, j, m));
            }
            C.add(row);
        }
    }

    private double findCij(int i, int j, int m) {
        double d = findKd(i, j, m);
        double f = findKf(i, j, m);
        double fAndD = operations.findCompose(f, d);
        System.out.println("f_d: " + fAndD);

        double term1 = operations.mult(
                operations.mult(f,
                        operations.diff(
                                operations.mult(3, G.get(i).get(j)),
                                2
                        )
                ),
                G.get(i).get(j)
        );

        double term2 = operations.mult(
                operations.sum(d,
                        operations.mult(
                                operations.diff(
                                        operations.mult(4, fAndD),
                                        operations.mult(3, d)
                                ),
                                G.get(i).get(j)
                        )
                ),
                operations.diff(1, G.get(i).get(j))
        );

        Tn += 1 * MatrixOperations.tComparison;
        Tn += (int) Math.ceil(3.0 / n) * MatrixOperations.tMult;
        Tn += (int) Math.ceil(3.0 / n) * MatrixOperations.tDiff;
        Tn += (int) Math.ceil(2.0 / n) * MatrixOperations.tMult;
        Tn += 1 * MatrixOperations.tMult;
        Tn += (int) Math.ceil(2.0 / n) * MatrixOperations.tMult;
        Tn += 1 * MatrixOperations.tSum;

        double cij = operations.sum(term1, term2);
        System.out.printf("C[%d][%d] = %.3f%n%n", i, j, cij);
        return cij;
    }

    private double findKf(int i, int j, int m) {
        List<Double> multiplArr = new ArrayList<>();
        int oldTn = Tn;

        for (int k = 0; k < m; k++) {
            double aToB = operations.findImpl(A.get(i).get(k), B.get(k).get(j));
            double bToA = operations.findImpl(B.get(k).get(j), A.get(i).get(k));

            double temp1 = operations.mult(
                    operations.mult(aToB,
                            operations.diff(
                                    operations.mult(2, E.get(0).get(k)),
                                    1
                            )
                    ),
                    E.get(0).get(k)
            );

            double temp2 = operations.mult(
                    operations.mult(bToA,
                            operations.sum(1,
                                    operations.mult(
                                            operations.diff(
                                                    operations.mult(4, aToB),
                                                    2
                                            ),
                                            E.get(0).get(k)
                                    )
                            )
                    ),
                    operations.diff(1, E.get(0).get(k))
            );

            System.out.printf("f00%d: %.3f%n", k, temp1 + temp2);
            multiplArr.add(operations.sum(temp1, temp2));

            Tn += (int) Math.ceil(3.0 / n) * MatrixOperations.tDiff;
            Tn += 1 * MatrixOperations.tMult;
            Tn += (int) Math.ceil(2.0 / n) * MatrixOperations.tComparison;
            Tn += 1 * MatrixOperations.tDiff;
            Tn += (int) Math.ceil(2.0 / n) * MatrixOperations.tDiff;
            Tn += (int) Math.ceil(2.0 / n) * MatrixOperations.tMult;
            Tn += 1 * MatrixOperations.tMult;
            Tn += 1 * MatrixOperations.tSum;
            Tn += 1 * MatrixOperations.tMult;
            Tn += 1 * MatrixOperations.tMult;
            Tn += 1 * MatrixOperations.tSum;
        }

        if (6 <= n && n <= m * 3) {
            int newN = n - n % 3;
            int count = (int) Math.ceil((m * 3.0) / newN);
            double temp = (double) (Tn - oldTn) / m;
            Tn -= (m - count) * temp;
        } else if (n >= m * 3) {
            double temp = (double) (Tn - oldTn) / m;
            Tn = oldTn + (int) temp;
        }

        double kf = multiplArr.get(0);
        for (int iMult = 1; iMult < multiplArr.size(); iMult++) {
            kf = operations.mult(kf, multiplArr.get(iMult));
        }

        Tn += (int) Math.ceil(m - 1) * MatrixOperations.tMult;

        System.out.println("kf: " + kf);
        return kf;
    }

    private double findKd(int i, int j, int m) {
        List<Double> multiplArr = new ArrayList<>();
        int oldTn = Tn;

        for (int k = 0; k < m; k++) {
            double temp1 = operations.findTnorm(A.get(i).get(k), B.get(k).get(j));
            System.out.printf("d00%d: %.3f%n", k, temp1);
            double temp2 = operations.diff(1, temp1);
            multiplArr.add(temp2);

            Tn += 1 * MatrixOperations.tComparison;
            Tn += 1 * MatrixOperations.tDiff;
        }

        if (2 <= n && n <= m * 1) {
            int newN = n - n % 1;
            int count = (int) Math.ceil((m * 1.0) / newN);
            double temp = (double) (Tn - oldTn) / m;
            Tn -= (m - count) * temp;
        } else if (n >= m * 1) {
            double temp = (double) (Tn - oldTn) / m;
            Tn = oldTn + (int) temp;
        }

        double ddRes = multiplArr.get(0);
        for (int iMult = 1; iMult < multiplArr.size(); iMult++) {
            ddRes = operations.mult(ddRes, multiplArr.get(iMult));
        }
        double dd = operations.diff(1, ddRes);
        System.out.println("kd: " + dd);

        Tn += (int) Math.ceil(m - 1) * MatrixOperations.tMult;
        Tn += 1 * MatrixOperations.tDiff;

        return dd;
    }

    public int findTavg() {
        int localTavg = 0;
        localTavg += p * q * m * (3 * (MatrixOperations.tDiff + MatrixOperations.tComparison) +
                7 * MatrixOperations.tMult + 3 * MatrixOperations.tDiff + 2 * MatrixOperations.tSum);
        localTavg += p * q * m * MatrixOperations.tComparison;
        localTavg += p * q * (m - 1) * MatrixOperations.tMult;
        localTavg += p * q * ((m + 1) * MatrixOperations.tDiff + (m - 1) * MatrixOperations.tMult);
        localTavg += p * q * (7 * MatrixOperations.tMult + 2 * MatrixOperations.tSum +
                3 * MatrixOperations.tDiff + MatrixOperations.tComparison);
        return localTavg;
    }

    public int calculateT1() {
        return operations.getMultCall() * MatrixOperations.tMult +
                operations.getDiffCall() * MatrixOperations.tDiff +
                operations.getSumCall() * MatrixOperations.tSum +
                operations.getCompareCall() * MatrixOperations.tComparison;
    }

    // Геттеры
    public List<List<Double>> getA() { return A; }
    public List<List<Double>> getB() { return B; }
    public List<List<Double>> getE() { return E; }
    public List<List<Double>> getG() { return G; }
    public List<List<Double>> getC() { return C; }
    public int getTn() { return Tn; }
    public int getP() { return p; }
    public int getQ() { return q; }
    public int getM() { return m; }
    public int getN() { return n; }
}
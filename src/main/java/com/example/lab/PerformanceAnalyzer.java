//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 9
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код анализирующий производительность
//28.04.2025

package com.example.lab;

public class PerformanceAnalyzer {
    private final MatrixCalculator calculator;
    private int T1;
    private int Tn;
    private double Ky;
    private double Eff;
    private double Diff;
    private int Lavg;

    private static final int timeOfSumm = 1;
    private static final int timeOfDifference = 1;
    private static final int timeOfMultiplying = 1;
    private static final int timeOfCom = 1;

    public PerformanceAnalyzer(MatrixCalculator calculator) {
        this.calculator = calculator;
    }

    public void calculatePerformance() {
        calculateTn();
        calculateT1();
        calculateKyAndEff();
        calculateLavgAndDiff();
    }

    private void calculateTn() {
        int r = calculator.getR();
        int p = calculator.getP();
        int q = calculator.getQ();
        int m = calculator.getM();
        int n = calculator.getN();

        // Вычисления для F
        int reductionTime = 3 * (timeOfMultiplying + timeOfDifference + timeOfSumm);
        int operationTime = 7 * timeOfMultiplying + 3 * timeOfDifference + 2 * timeOfSumm;
        Tn = (reductionTime + operationTime) * (int) Math.ceil((double) r / n);

        // Вычисления для D
        Tn += timeOfMultiplying * (int) Math.ceil((double) r / n);

        // Вычисления для C
        int FTime = 2 * timeOfMultiplying * (m - 1);
        int DTime = 3 * (timeOfDifference * (m + 1) + timeOfMultiplying * (m - 1));
        operationTime = 8 * timeOfMultiplying + 4 * timeOfDifference + 2 * timeOfSumm;
        Tn += (FTime + DTime + operationTime) * (int) Math.ceil((double) (p * q) / (double) n);
    }

    private void calculateT1() {
        T1 = timeOfSumm * calculator.getCallsOfSumm() +
                timeOfDifference * calculator.getCallsOfDifference() +
                timeOfMultiplying * calculator.getCallsOfMultiplying() +
                timeOfCom * calculator.getCallsOfCom();

        if (Tn > T1) {
            Tn = T1;
        }
    }

    private void calculateKyAndEff() {
        Ky = (double) T1 / Tn;
        Eff = Ky / calculator.getN();
    }

    private void calculateLavgAndDiff() {
        int r = calculator.getR();
        int p = calculator.getP();
        int q = calculator.getQ();
        int m = calculator.getM();

        Lavg = timeOfMultiplying * r;
        Lavg += (7 * timeOfMultiplying + 3 * timeOfDifference + 2 * timeOfSumm) * r;
        Lavg += (8 * timeOfMultiplying + 3 * timeOfDifference + 2 * timeOfSumm) * p * q;
        Lavg += (timeOfCom + timeOfSumm + timeOfDifference) * (m - 1) * 2 * p * q;
        Lavg += (timeOfMultiplying * (m - 1) + timeOfDifference * (m + 1)) * 3 * p * q;
        Lavg += (timeOfCom) * r * 3;
        Lavg = (int) Math.ceil((double) Lavg / r);
        Diff = (double) Tn / Lavg;
    }

    // Геттеры для результатов анализа
    public int getT1() {
        return T1;
    }

    public int getTn() {
        return Tn;
    }

    public double getKy() {
        return Ky;
    }

    public double getEff() {
        return Eff;
    }

    public int getLavg() {
        return Lavg;
    }

    public double getDiff() {
        return Diff;
    }
}
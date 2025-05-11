//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 9
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код выполняющий математические операции
//28.04.2025
package com.example.lab;

public class MatrixOperations {
    public static final int tSum = 1;
    public static final int tMult = 1;
    public static final int tDiff = 1;
    public static final int tComparison = 1;

    private int sumCall = 0;
    private int multCall = 0;
    private int diffCall = 0;
    private int compareCall = 0;

    public double sum(double a, double b) {
        sumCall++;
        return a + b;
    }

    public double mult(double a, double b) {
        multCall++;
        return a * b;
    }

    public double diff(double a, double b) {
        diffCall++;
        return a - b;
    }

    public double compare(double a, double b, boolean maxOrMin) {
        compareCall++;
        return maxOrMin ? Math.max(a, b) : Math.min(a, b);
    }

    public double findCompose(double a, double b) {
        return compare(a, b, false);
    }

    public double findTnorm(double a, double b) {
        return compare(a, b, false);
    }

    public double findImpl(double a, double b) {
        return compare(diff(1, a), b, true);
    }

    public double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public int getSumCall() { return sumCall; }
    public int getMultCall() { return multCall; }
    public int getDiffCall() { return diffCall; }
    public int getCompareCall() { return compareCall; }

    public void resetCounters() {
        sumCall = 0;
        multCall = 0;
        diffCall = 0;
        compareCall = 0;
    }
}
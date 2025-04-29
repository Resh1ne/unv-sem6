//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 9
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код отвечающий за отображение в консоли
//28.04.2025

package com.example.lab;

public class ResultPrinter {
    private final MatrixCalculator calculator;

    public ResultPrinter(MatrixCalculator calculator) {
        this.calculator = calculator;
    }

    public void printAllResults() {
        System.out.println("Matrix A:");
        printMatrix(calculator.getA());

        System.out.println("\nMatrix B:");
        printMatrix(calculator.getB());

        System.out.println("\nVector E:");
        printArray(calculator.getE());

        System.out.println("\nMatrix G:");
        printMatrix(calculator.getG());

        System.out.println("\nResult Matrix C:");
        printMatrix(calculator.getC());
    }

    public void printPerformanceResults(PerformanceAnalyzer analyzer) {
        System.out.println("\nPerformance Parameters:");
        System.out.println("T1= " + analyzer.getT1());
        System.out.println("Tn= " + analyzer.getTn());
        System.out.println("Ky= " + analyzer.getKy());
        System.out.println("e= " + analyzer.getEff());
        System.out.println("Lsum= " + analyzer.getTn());
        System.out.println("Lavg= " + analyzer.getLavg());
        System.out.println("D= " + analyzer.getDiff());
    }

    private void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                System.out.printf("%12.6f ", val);
            }
            System.out.println();
        }
    }

    private void printArray(double[] array) {
        for (double val : array) {
            System.out.printf("%12.6f ", val);
        }
        System.out.println();
    }
}

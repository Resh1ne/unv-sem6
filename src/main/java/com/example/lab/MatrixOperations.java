//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 9
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код реализующий матричные операции
//28.04.2025

package com.example.lab;

import java.util.Scanner;

public class MatrixOperations {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input m,p,q,n");
        int m = scanner.nextInt();
        int p = scanner.nextInt();
        int q = scanner.nextInt();
        int n = scanner.nextInt();
        scanner.close();

        // Инициализация всех компонентов
        MatrixCalculator calculator = new MatrixCalculator(m, p, q, n);
        PerformanceAnalyzer analyzer = new PerformanceAnalyzer(calculator);

        // Выполнение вычислений
        calculator.computeAll();

        // Вывод результатов
        ResultPrinter printer = new ResultPrinter(calculator);
        printer.printAllResults();

        // Анализ производительности
        analyzer.calculatePerformance();
        printer.printPerformanceResults(analyzer);
    }
}
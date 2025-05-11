//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 9
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Главный класс
//28.04.2025
package com.example.lab;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MatrixOperations operations = new MatrixOperations();
        MatrixCalculator calculator = new MatrixCalculator(operations);
        MatrixIO io = new MatrixIO();

        while (true) {
            System.out.print("m = ");
            int m = Integer.parseInt(scanner.nextLine());
            System.out.print("p = ");
            int p = Integer.parseInt(scanner.nextLine());
            System.out.print("q = ");
            int q = Integer.parseInt(scanner.nextLine());
            System.out.print("n = ");
            int n = Integer.parseInt(scanner.nextLine());

            if (io.checkInput(String.valueOf(m) + p + q + n)) {
                System.out.println("Некорректный ввод");
            } else if (n == 0 || p == 0 || m == 0 || q == 0) {
                System.out.println("Введите значения больше 0");
            } else {
                operations.resetCounters();
                calculator.fillMatrix(m, p, q);
                calculator.findC(p, q, m, n);
                break;
            }
        }

        int T1 = calculator.calculateT1();
        double Ky = (double) T1 / calculator.getTn();
        double e = Ky / calculator.getN();
        int r = calculator.getP() * calculator.getQ() +
                calculator.getP() * calculator.getM() +
                calculator.getQ() * calculator.getM() +
                1 * calculator.getM() +
                calculator.getP() * calculator.getQ();

        double Tavg = calculator.findTavg();
        double Lavg = Tavg / r;
        double D = (double) calculator.getTn() / Lavg;

        io.printMatrix(calculator.getA(), "\nA:");
        io.printMatrix(calculator.getB(), "\nB:");
        io.printMatrix(calculator.getE(), "\nE:");
        io.printMatrix(calculator.getG(), "\nG:");
        io.printMatrix(calculator.getC(), "\nC:");

        System.out.println("\nParameters:");
        System.out.println("T1 = " + T1);
        System.out.println("Tn = " + calculator.getTn());
        System.out.println("r = " + r);
        System.out.println("Ky = " + Ky);
        System.out.println("e = " + e);
        System.out.println("Lsum = " + calculator.getTn());
        System.out.println("Lavg = " + Lavg);
        System.out.println("D = " + D);

        scanner.close();
    }
}
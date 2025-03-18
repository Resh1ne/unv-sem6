//Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 5. Алгоритм вычисления целочисленного частного пары 4-разрядных чисел делением с восстановлением остатка
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код алгоритм деления с восстановлением остатка
//17.03.2025

package com.example.pipeline;

public class BinaryDivision {
    private final BinaryNumber dividend;
    private final BinaryNumber divisor;
    private StringBuilder remainder;
    private StringBuilder quotient;
    private int iteration;

    public BinaryDivision(BinaryNumber dividend, BinaryNumber divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
        this.iteration = 0;
        this.remainder = new StringBuilder("00000");
        this.quotient = new StringBuilder(dividend.getBits());
    }

    public void execute() {
        if (iteration >= 4) {
            return;
        }

        StringBuilder shiftNumber = new StringBuilder(remainder).append(quotient);
        shiftNumber = new StringBuilder(shiftNumber.substring(1) + "0");
        remainder = new StringBuilder(shiftNumber.substring(0, 5));
        quotient = new StringBuilder(shiftNumber.substring(5));

        BinaryNumber subtractionResult = new BinaryNumber(remainder.toString()).diff(divisor);

        if (subtractionResult.isNegative()) {
            quotient.setCharAt(quotient.length() - 1, '0');
        } else {
            remainder = new StringBuilder(subtractionResult.toBinaryString());
            quotient.setCharAt(quotient.length() - 1, '1');
        }

        iteration++;
        System.out.println("Iteration: " + iteration + ", Remainder: " + remainder.substring(1) + ", Quotient: " + quotient);
    }

    public boolean isFinished() {
        return iteration >= 4;
    }

    public void printInput() {
        System.out.println("Dividend: " + dividend.toBinaryString() + ", Divisor: " + divisor.toBinaryString());
    }

    public void printContent() {
        System.out.println("Remainder: " + remainder + ", Quotient: " + quotient);
    }

    public void printOutput() {
        System.out.printf("Result: Quotient = %s (%s), Remainder = %s (%s)%n", quotient.toString(),
                new BinaryNumber(quotient.toString()).toDecimal(), remainder.toString(), new BinaryNumber(remainder.toString()).toDecimal());
    }

    public static void printEmpty() {
        System.out.println("-");
    }
}

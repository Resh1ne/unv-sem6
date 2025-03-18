package com.example.pipeline;

public class BinaryDivision {
    private final BinaryNumber dividend;
    private final BinaryNumber divisor;
    private StringBuilder remainder;
    private StringBuilder quotient;
    private int iteration; // Текущая итерация

    public BinaryDivision(BinaryNumber dividend, BinaryNumber divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
        this.iteration = 0;
        this.remainder = new StringBuilder("00000");
        this.quotient = new StringBuilder(dividend.getBits());
    }

    public void execute() {
        if (divisor.toDecimal() == 0) {
            throw new ArithmeticException("Деление на ноль");
        }

        if (iteration >= 4) {
            return; // Вычисление завершено
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
        System.out.println("Dividend: " + dividend + ", Divisor: " + divisor);
    }

    public void printContent() {
        System.out.println("Remainder: " + remainder + ", Quotient: " + quotient);
    }

    public void printOutput() {
        System.out.println("Result: Quotient = " + quotient.toString() + ", Remainder = " + remainder.toString());
    }

    public static void printEmpty() {
        System.out.println("-");
    }

//    public static void main(String[] args) {
//        BinaryNumber dividend = BinaryNumber.fromDecimal(15);
//        BinaryNumber divisor = BinaryNumber.fromDecimal(1);
//
//        BinaryDivision div = new BinaryDivision(dividend, divisor);
//        div.execute();
//    }
}

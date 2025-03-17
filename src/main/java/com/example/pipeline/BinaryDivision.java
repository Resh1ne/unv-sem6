package com.example.pipeline;

public class BinaryDivision {

    public static BinaryNumber[] divide(BinaryNumber dividend, BinaryNumber divisor) {
        if (divisor.toDecimal() == 0) {
            throw new ArithmeticException("Деление на ноль");
        }

        StringBuilder remainder = new StringBuilder("00000");
        StringBuilder quotient = new StringBuilder(dividend.getBits());


        // Основной цикл (4 шага)
        for (int i = 0; i < 4; i++) {
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
            System.out.println("Remainder: " + remainder.substring(1));
            System.out.println("Quotient: " + quotient);
        }

        return new BinaryNumber[]{new BinaryNumber(quotient.toString()), new BinaryNumber(remainder.toString())};
    }

    public static void main(String[] args) {
        BinaryNumber dividend = BinaryNumber.fromDecimal(15);
        BinaryNumber divisor = BinaryNumber.fromDecimal(1);

        BinaryNumber[] results = divide(dividend, divisor);
        System.out.println("Частное: " + results[0].toBinaryString() + " (десятичное: " + results[0].toDecimal() + ")");
        System.out.println("Остаток: " + results[1].toBinaryString() + " (десятичное: " + results[1].toDecimal() + ")");
    }
}

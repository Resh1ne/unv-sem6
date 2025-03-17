package com.example.pipeline;

public class BinaryNumber {
    private final String binaryString;

    public BinaryNumber(String binaryString) {
        if (binaryString.length() != 5) {
            throw new IllegalArgumentException("Длина строки должна быть равна 5 (знаковый бит + 4 бита)");
        }
        if (!binaryString.matches("[01]{5}")) {
            throw new IllegalArgumentException("Строка должна содержать только символы '0' и '1'");
        }
        this.binaryString = binaryString;
    }

    public boolean isNegative() {
        return binaryString.charAt(0) == '1';
    }

    public String getBits() {
        return binaryString.substring(1);
    }

    public String toBinaryString() {
        return binaryString;
    }

    public int toDecimal() {
        int decimalValue = 0;
        String bits = getBits();
        for (int i = 0; i < bits.length(); i++) {
            decimalValue += (bits.charAt(bits.length() - 1 - i) - '0') * (1 << i);
        }
        return isNegative() ? -decimalValue : decimalValue;
    }

    public static BinaryNumber fromDecimal(int decimal) {
        if (decimal < -15 || decimal > 15) {
            throw new IllegalArgumentException("Число должно быть в диапазоне от -15 до 15");
        }

        boolean isNegative = decimal < 0;
        int absoluteValue = Math.abs(decimal);

        StringBuilder binaryBits = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            int bit = (absoluteValue >> i) & 1;
            binaryBits.append(bit);
        }

        String binaryString = (isNegative ? "1" : "0") + binaryBits;
        return new BinaryNumber(binaryString);
    }

    public String toTwosComplement() {
        StringBuilder invertedBits = new StringBuilder();
        for (char bit : binaryString.toCharArray()) {
            invertedBits.append(bit == '0' ? '1' : '0');
        }

        StringBuilder twosComplement = new StringBuilder(invertedBits.toString());
        for (int i = twosComplement.length() - 1; i >= 0; i--) {
            if (twosComplement.charAt(i) == '0') {
                twosComplement.setCharAt(i, '1');
                break;
            } else if (twosComplement.charAt(i) == '1') {
                twosComplement.setCharAt(i, '0');
            }
        }

        return twosComplement.toString();
    }

    public BinaryNumber sum(BinaryNumber other) {
        String bits1 = this.toBinaryString();
        String bits2 = other.toBinaryString();

        StringBuilder resultBits = new StringBuilder();
        int carry = 0;
        for (int i = 4; i >= 0; i--) {
            int bit1 = bits1.charAt(i) - '0';
            int bit2 = bits2.charAt(i) - '0';
            int sum = bit1 + bit2 + carry;
            resultBits.insert(0, sum % 2);
            carry = sum / 2;
        }

        return new BinaryNumber(resultBits.toString());
    }

    public BinaryNumber diff(BinaryNumber other) {
        return sum(new BinaryNumber(other.toTwosComplement()));
    }

    public static void main(String[] args) {
        BinaryNumber number = BinaryNumber.fromDecimal(-14);

        System.out.println("Двоичное представление: " + number.toBinaryString());
        System.out.println("Знаковый бит: " + (number.isNegative() ? "Отрицательное" : "Положительное"));
        System.out.println("Биты числа: " + number.getBits());
        System.out.println("Десятичное значение: " + number.toDecimal());
        System.out.println("Дополнительный код: " + number.toTwosComplement());
        System.out.println("================");
        BinaryNumber number2 = BinaryNumber.fromDecimal(3);
        System.out.println("Двоичное представление: " + number2.toBinaryString());
        System.out.println("Десятичное значение: " + number2.toDecimal());
        System.out.println("Дополнительный код: " + number2.toTwosComplement());
        System.out.println("================");
        System.out.println("Первое число: " + number.toBinaryString() + " (десятичное: " + number.toDecimal() + ")");
        System.out.println("Второе число: " + number2.toBinaryString() + " (десятичное: " + number2.toDecimal() + ")");
        BinaryNumber sum = number.sum(number2);
        System.out.println("Результат сложения: " + sum.toBinaryString() + " (десятичное: " + sum.toDecimal() + ")");
        System.out.println("================");
        BinaryNumber diff = number.diff(number2);
        System.out.println("Результат разности: " + diff.toBinaryString() + " (десятичное: " + diff.toDecimal() + ")");
    }
}
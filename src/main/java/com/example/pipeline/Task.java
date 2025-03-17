package com.example.pipeline;

public class Task {
    private final int dividend;
    private final int divisor;
    private int quotient;
    private int remainder;
    private int bitIndex;

    public Task(int dividend, int divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
        this.quotient = 0;
        this.remainder = 0;
        this.bitIndex = 3; // 4-битное число, начинаем с 3-го бита
    }

    public void executeStep() {
        if (bitIndex < 0) return; // Если все биты обработаны, ничего не делаем

        // Сдвиг остатка и добавление очередного бита делимого
        remainder = (remainder << 1) | ((dividend >> bitIndex) & 1);

        remainder -= divisor; // Вычитаем делитель
        quotient <<= 1; // Сдвиг частного влево

        if (remainder >= 0) {
            quotient |= 1; // Записываем 1 в частное
        } else {
            remainder += divisor; // Восстанавливаем остаток
        }

        bitIndex--; // Переход к следующему биту
    }

    public boolean isComplete() {
        return bitIndex < 0;
    }

    public int getResult() {
        return quotient;
    }

    public int getPartialQuotient() {
        return quotient;
    }

    public int getPartialRemainder() {
        return remainder;
    }

    @Override
    public String toString() {
        return String.format("(%d / %d = %s, Частичное частное: %4s, Частичный остаток: %4s)",
                dividend, divisor, isComplete() ? Integer.toBinaryString(quotient) : "?",
                String.format("%4s", Integer.toBinaryString(quotient)).replace(' ', '0'),
                String.format("%4s", Integer.toBinaryString(remainder)).replace(' ', '0'));
    }
}

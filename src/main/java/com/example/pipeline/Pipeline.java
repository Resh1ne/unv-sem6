package com.example.pipeline;

import java.util.ArrayDeque;
import java.util.Deque;

public class Pipeline {
    private final int itemsSize;
    private final Deque<BinaryDivision> input;
    private final BinaryDivision[] steps;
    private final Deque<BinaryDivision> output;

    public Pipeline(int itemsSize, int stepsCount) {
        this.itemsSize = itemsSize;
        this.input = new ArrayDeque<>();
        this.steps = new BinaryDivision[stepsCount];
        this.output = new ArrayDeque<>();
    }

    public void addInput(BinaryDivision item) {
        if (input.size() < itemsSize) {
            input.addLast(item);
        } else {
            throw new IllegalStateException("Input queue is full");
        }
    }

    public void tick() {
        // Если последний шаг завершил вычисления, перемещаем его в output и очищаем шаг
        if (steps[steps.length - 1] != null && steps[steps.length - 1].isFinished()) {
            output.addLast(steps[steps.length - 1]); // Перемещаем в выходную очередь
            steps[steps.length - 1] = null; // Явно очищаем последний шаг
        }

        // Сдвигаем шаги вправо
        for (int i = steps.length - 1; i > 0; i--) {
            steps[i] = steps[i - 1];
        }

        // Очищаем первый шаг после сдвига
        steps[0] = null;

        // Перемещаем объект из input в первый шаг, если input не пуст
        if (!input.isEmpty()) {
            steps[0] = input.removeFirst();
        }

        // Выполняем вычисления для каждого шага
        for (int i = steps.length - 1; i >= 0; i--) {
            if (steps[i] != null) {
                steps[i].execute();
            }
        }
    }

    public void preTick() {
        clearConsole();
        System.out.println("Входная очередь:");
        for (int i = itemsSize - 1; i >= 0; i--) {
            if (i < input.size()) {
                input.toArray(new BinaryDivision[0])[i].printInput();
            } else {
                System.out.println("-");
            }
        }

        for (int i = 0; i < steps.length; i++) {
            System.out.println("Этап " + (i + 1) + ":");
            if (steps[i] != null) {
                steps[i].printContent();
            } else {
                BinaryDivision.printEmpty();
            }
        }

        System.out.println("Результат:");
        for (BinaryDivision item : output) {
            item.printOutput();
        }
        for (int i = 0; i < itemsSize - output.size(); i++) {
            System.out.println("-");
        }

        System.out.print("Нажмите Enter для продолжения: ");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (output.size() < itemsSize || !input.isEmpty() || hasActiveSteps()) {
            preTick();
            tick();
        }
        preTick();
    }

    private boolean hasActiveSteps() {
        for (BinaryDivision step : steps) {
            if (step != null && !step.isFinished()) {
                return true;
            }
        }
        return false;
    }

    public static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (final Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline(4, 4);

        // Добавляем входные данные
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(12), BinaryNumber.fromDecimal(3)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(14), BinaryNumber.fromDecimal(2)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(13), BinaryNumber.fromDecimal(3)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(12), BinaryNumber.fromDecimal(4)));

        // Запускаем конвейер
        pipeline.run();
    }
}
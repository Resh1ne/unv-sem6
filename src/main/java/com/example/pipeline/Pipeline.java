//Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 5. Алгоритм вычисления целочисленного частного пары 4-разрядных чисел делением с восстановлением остатка
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код описывает конвеер
//17.03.2025

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
        if (steps[steps.length - 1] != null && steps[steps.length - 1].isFinished()) {
            output.addLast(steps[steps.length - 1]);
            steps[steps.length - 1] = null;
        }

        for (int i = steps.length - 1; i > 0; i--) {
            steps[i] = steps[i - 1];
        }

        steps[0] = null;

        if (!input.isEmpty()) {
            steps[0] = input.removeFirst();
        }

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
            System.in.skip(System.in.available());
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
        Pipeline pipeline = new Pipeline(6, 4);

        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(15), BinaryNumber.fromDecimal(1)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(14), BinaryNumber.fromDecimal(2)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(13), BinaryNumber.fromDecimal(3)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(12), BinaryNumber.fromDecimal(4)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(11), BinaryNumber.fromDecimal(5)));
        pipeline.addInput(new BinaryDivision(BinaryNumber.fromDecimal(10), BinaryNumber.fromDecimal(6)));

        pipeline.run();
    }
}
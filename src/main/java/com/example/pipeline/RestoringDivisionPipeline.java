package com.example.pipeline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class RestoringDivisionPipeline {
    private static int M; // Длина векторов
    private static final int MAX_VALUE = 15; // 4-разрядные числа (0-15)
    private static final int PIPELINE_STAGES = 4; // Количество этапов конвейера

    private final Queue<Task> inputQueue = new LinkedList<>();
    private final Queue<Task>[] pipelineStages = new LinkedList[PIPELINE_STAGES];
    private final List<Task> outputQueue = new ArrayList<>();

    // Заранее инициализированные вектора делимых и делителей
    private static final int[] dividends = {12, 9, 15, 7, 10, 13, 8, 5, 11, 13};
    private static final int[] divisors = {3, 2, 5, 1, 2, 3, 4, 1, 3, 14}; // Один из делителей равен 0 для проверки

    public static void main(String[] args) {
        RestoringDivisionPipeline pipeline = new RestoringDivisionPipeline();
        pipeline.run();
    }

    public RestoringDivisionPipeline() {
        checkException();

        // Инициализация векторов делимых и делителей
        for (int i = 0; i < M; i++) {
            inputQueue.add(new Task(dividends[i], divisors[i]));
        }

        // Инициализация этапов конвейера
        for (int i = 0; i < PIPELINE_STAGES; i++) {
            pipelineStages[i] = new LinkedList<>();
        }
    }

    private static void checkException() {
        for (int divisor : divisors) {
            if (divisor > MAX_VALUE) {
                throw new IllegalArgumentException("Число " + divisor + " не 4-х разрядное");
            }
        }

        for (int dividend : dividends) {
            if (dividend > MAX_VALUE) {
                throw new IllegalArgumentException("Число " + dividend + " не 4-х разрядное");
            }
        }
        if (dividends.length != divisors.length) {
            throw new IllegalArgumentException("Массивы делимых и делителей должны быть одинаковой длины!");
        } else {
            M = dividends.length;
        }

        for (int divisor : divisors) {
            if (divisor == 0) {
                throw new IllegalArgumentException("Нельзя делить на ноль!");
            }
        }
    }

    public void run() {
        while (outputQueue.size() < M) {
            preTick();
            tick();
        }
        preTick();
    }

    private void tick() {
        // Перенос данных по конвейеру
        if (!pipelineStages[PIPELINE_STAGES - 1].isEmpty()) {
            outputQueue.add(pipelineStages[PIPELINE_STAGES - 1].poll());
        }
        for (int i = PIPELINE_STAGES - 1; i > 0; i--) {
            if (!pipelineStages[i - 1].isEmpty()) {
                pipelineStages[i].add(pipelineStages[i - 1].poll());
            }
        }

        // Добавление новой задачи в первый этап
        if (!inputQueue.isEmpty()) {
            pipelineStages[0].add(inputQueue.poll());
        }

        // Выполнение обработки на каждом этапе
        for (Queue<Task> stage : pipelineStages) {
            if (!stage.isEmpty()) {
                stage.peek().executeStep();
            }
        }
    }

    private void preTick() {
        System.out.println("\n===== Конвейер =====");

        System.out.println("Входная очередь: " + QueueUtils.queueToString(inputQueue));
        for (int i = 0; i < PIPELINE_STAGES; i++) {
            System.out.println("Этап " + (i + 1) + ": " + QueueUtils.queueDetailedString(pipelineStages[i]));
        }
        System.out.println("Результат: " + QueueUtils.outputQueueToString(outputQueue));

        System.out.print("Нажмите Enter для продолжения...");
        new Scanner(System.in).nextLine();
    }
}
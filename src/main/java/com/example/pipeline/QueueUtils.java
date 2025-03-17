package com.example.pipeline;

import java.util.List;
import java.util.Queue;

public class QueueUtils {
    public static String queueToString(Queue<Task> queue) {
        return queue.isEmpty() ? "-" : queue.toString();
    }

    public static String outputQueueToString(List<Task> outputQueue) {
        if (outputQueue.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (Task task : outputQueue) {
            sb.append(task.getResult()).append(" ");
        }
        return sb.toString();
    }

    public static String queueDetailedString(Queue<Task> queue) {
        if (queue.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (Task task : queue) {
            sb.append(String.format("[Частичное частное: %s, Частичный остаток: %s] ",
                    formatAs4Bit(task.getPartialQuotient()),
                    formatAs4Bit(task.getPartialRemainder())));
        }
        return sb.toString();
    }

    private static String formatAs4Bit(int value) {
        return String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}
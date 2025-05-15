//Лабораторная работа №2 по дисциплине Модели решения задач в интеллектуальных системах
//Вариант 9
//Выполена студентом группы 221702 БГУИР Потоцкий Даниил Александрович
//Код отвечающий за ввод/вывод
//1.Ивашенко В.П.-Формальные модели обработки информации и параллельные модели решения задач:
//учеб.-метод.пособие/В.П.Ивашенко.-Минск:БГУИР,2020.-79с.
//28.04.2025
package com.example.lab;

import java.util.List;

public class MatrixIO {

    public boolean checkInput(String str) {
        for (char c : str.toCharArray()) {
            if (!String.valueOf(c).matches("[0-9]")) {
                return true;
            }
        }
        return false;
    }

    public void printMatrix(List<List<Double>> matrix, String name) {
        System.out.println(name);
        for (List<Double> row : matrix) {
            StringBuilder sb = new StringBuilder("   ");
            for (Double col : row) {
                sb.append(String.format("%.3f", col)).append("  ");
            }
            System.out.println(sb);
        }
    }
}
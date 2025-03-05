package com.example.graphicseditor;

import com.example.algorithms.lb4.Matrix;
import com.example.algorithms.lb4.Object3D;
import com.example.algorithms.lb4.ObjectLoader;
import com.example.algorithms.lb4.KeyboardHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Editor3DPanel extends JPanel {
    private Object3D object3D;

    public Editor3DPanel() {
        setBackground(Color.WHITE);
        setFocusable(true);

        try {
            object3D = ObjectLoader.loadFromFile("other.obj");

            // Начальное масштабирование и смещение объекта
            Matrix scale = Matrix.scaling(50, 50, 50); // Уменьшим начальный масштаб
            Matrix translate = Matrix.translation(0, 0, -10); // Смещаем объект вперед
            object3D.transform(scale.multiply(translate)); // Применяем преобразования
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки файла!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            object3D = new Object3D();  // Создаем пустой объект в случае ошибки
        }

        // Передаем текущую панель (this) в KeyboardHandler
        addKeyListener(new KeyboardHandler(object3D, this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (object3D != null) {
            Matrix projection = Matrix.perspective(60, (double) getWidth() / getHeight(), 0.1, 100);

            List<double[]> vertices = object3D.getVertices();
            List<int[]> faces = object3D.getFaces();

            // Сортируем грани по их средней z-координате (задние грани рисуются первыми)
            faces.sort((face1, face2) -> {
                double z1 = 0, z2 = 0;
                for (int i : face1) z1 += vertices.get(i)[2];
                for (int i : face2) z2 += vertices.get(i)[2];
                return Double.compare(z2, z1); // Сортируем по убыванию
            });

            for (int[] face : faces) {
                for (int i = 0; i < face.length; i++) {
                    int nextIndex = (i + 1) % face.length;
                    double[] v1 = projection.transform(vertices.get(face[i]));
                    double[] v2 = projection.transform(vertices.get(face[nextIndex]));

                    // Проверка на видимость (z-координата должна быть положительной)
                    if (v1[2] > 0 && v2[2] > 0) {
                        int x1 = (int) ((v1[0] / v1[3] + 1) * getWidth() / 2);
                        int y1 = (int) ((1 - v1[1] / v1[3]) * getHeight() / 2);
                        int x2 = (int) ((v2[0] / v2[3] + 1) * getWidth() / 2);
                        int y2 = (int) ((1 - v2[1] / v2[3]) * getHeight() / 2);

                        g2d.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }
    }
}
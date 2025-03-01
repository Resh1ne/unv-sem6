package com.example.algorithms.lb4;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class KeyboardHandler extends KeyAdapter {
    private final Object3D object;
    private final JPanel panel;

    public KeyboardHandler(Object3D object, JPanel panel) {
        this.object = object;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> object.transform(Matrix4x4.translation(0, 0, -1)); // Вперед по Z
            case KeyEvent.VK_S -> object.transform(Matrix4x4.translation(0, 0, 1));  // Назад по Z
            case KeyEvent.VK_A -> object.transform(Matrix4x4.translation(-1, 0, 0)); // Влево по X
            case KeyEvent.VK_D -> object.transform(Matrix4x4.translation(1, 0, 0));  // Вправо по X
            case KeyEvent.VK_Q -> object.rotateY(-10); // Поворот влево вокруг центра
            case KeyEvent.VK_E -> object.rotateY(10);  // Поворот вправо вокруг центра
            case KeyEvent.VK_Z -> object.scale(1.2);  // Увеличение
            case KeyEvent.VK_X -> object.scale(0.8);   // Уменьшение
        }
        panel.repaint(); // Перерисовываем панель после трансформации
    }
}
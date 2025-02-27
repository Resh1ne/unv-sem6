package com.example.algorithms.lb4;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class KeyboardHandler extends KeyAdapter {
    private final Object3D object;
    private final JPanel panel; // Добавляем ссылку на панель

    public KeyboardHandler(Object3D object, JPanel panel) {
        this.object = object;
        this.panel = panel; // Инициализируем панель
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> object.transform(Matrix4x4.translation(0, 0, -0.1));
            case KeyEvent.VK_S -> object.transform(Matrix4x4.translation(0, 0, 0.1));
            case KeyEvent.VK_A -> object.transform(Matrix4x4.translation(-0.1, 0, 0));
            case KeyEvent.VK_D -> object.transform(Matrix4x4.translation(0.1, 0, 0));
            case KeyEvent.VK_Q -> object.transform(Matrix4x4.rotationY(-5));
            case KeyEvent.VK_E -> object.transform(Matrix4x4.rotationY(5));
            case KeyEvent.VK_Z -> object.transform(Matrix4x4.scaling(1.1, 1.1, 1.1));
            case KeyEvent.VK_X -> object.transform(Matrix4x4.scaling(0.9, 0.9, 0.9));
            case KeyEvent.VK_R -> object.transform(Matrix4x4.reflectionZ());
            case KeyEvent.VK_P -> object.transform(Matrix4x4.perspective(90, 1.0, 0.1, 100));
        }
        panel.repaint(); // Перерисовываем панель после трансформации
    }
}
package com.example.graphicseditor;

import javax.swing.*;
import java.awt.*;

public class GraphicsEditor extends JFrame {

    private final JTabbedPane tabbedPane;

    public GraphicsEditor() {
        setTitle("Графический редактор");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создаем вкладки
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Редактор", new EditorPanel());
        tabbedPane.addTab("Отладка", new DebugPanel());

        // Добавляем вкладки в окно
        add(tabbedPane);

        // Создаем панель инструментов
        JToolBar toolBar = new JToolBar();
        JButton editorButton = new JButton("Редактор");
        JButton debugButton = new JButton("Отладка");

        editorButton.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        debugButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));

        toolBar.add(editorButton);
        toolBar.add(debugButton);

        add(toolBar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicsEditor editor = new GraphicsEditor();
            editor.setVisible(true);
        });
    }
}
package com.example.graphicseditor;

import javax.swing.*;
import java.awt.*;

public class GraphicsEditor extends JFrame {

    private final EditorPanel editorPanel;

    public GraphicsEditor() {
        setTitle("Графический редактор");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создаем главные вкладки
        JTabbedPane mainTabbedPane = new JTabbedPane();
        editorPanel = new EditorPanel();
        mainTabbedPane.addTab("Редактор", editorPanel);
        mainTabbedPane.addTab("Отладка", new DebugPanel());

        // Добавляем главные вкладки в окно
        add(mainTabbedPane);

        // Создаем панель инструментов для выбора фигуры и алгоритма
        JToolBar toolBar = new JToolBar();
        JComboBox<ShapeType> shapeComboBox = new JComboBox<>(ShapeType.values());
        JComboBox<AlgorithmType> algorithmComboBox = new JComboBox<>(AlgorithmType.values());

        // Обработка выбора фигуры
        shapeComboBox.addActionListener(e -> {
            ShapeType selectedShape = (ShapeType) shapeComboBox.getSelectedItem();
            editorPanel.setShapeType(selectedShape);
            assert selectedShape != null;
            updateAlgorithmComboBox(algorithmComboBox, selectedShape);
        });

        // Обработка выбора алгоритма
        algorithmComboBox.addActionListener(e -> {
            AlgorithmType selectedAlgorithm = (AlgorithmType) algorithmComboBox.getSelectedItem();
            editorPanel.setAlgorithmType(selectedAlgorithm);
        });

        toolBar.add(new JLabel("Фигура: "));
        toolBar.add(shapeComboBox);
        toolBar.add(new JLabel("Алгоритм: "));
        toolBar.add(algorithmComboBox);

        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Обновляет доступные алгоритмы в зависимости от выбранной фигуры.
     *
     * @param algorithmComboBox Комбобокс с алгоритмами.
     * @param shapeType         Выбранная фигура.
     */
    private void updateAlgorithmComboBox(JComboBox<AlgorithmType> algorithmComboBox, ShapeType shapeType) {
        algorithmComboBox.removeAllItems();
        switch (shapeType) {
            case LINE:
                algorithmComboBox.addItem(AlgorithmType.DDA);
                algorithmComboBox.addItem(AlgorithmType.BRESENHAM);
                algorithmComboBox.addItem(AlgorithmType.WU);
                break;
            case CIRCLE:
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicsEditor editor = new GraphicsEditor();
            editor.setVisible(true);
        });
    }
}
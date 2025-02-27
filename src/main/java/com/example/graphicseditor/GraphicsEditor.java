package com.example.graphicseditor;

import javax.swing.*;
import java.awt.*;

public class GraphicsEditor extends JFrame {

    public GraphicsEditor() {
        setTitle("Графический редактор");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane mainTabbedPane = new JTabbedPane();
        EditorPanel editorPanel = new EditorPanel();
        DebugPanel debugPanel = new DebugPanel();
        Editor3DPanel editor3DPanel = new Editor3DPanel();

        mainTabbedPane.addTab("Редактор 2D", editorPanel);
        mainTabbedPane.addTab("Отладка", debugPanel);
        mainTabbedPane.addTab("Редактор 3D", editor3DPanel);

        add(mainTabbedPane);

        JToolBar toolBar = new JToolBar();
        JComboBox<ShapeType> shapeComboBox = new JComboBox<>(ShapeType.values());
        JComboBox<AlgorithmType> algorithmComboBox = new JComboBox<>(AlgorithmType.values());

        shapeComboBox.addActionListener(e -> {
            ShapeType selectedShape = (ShapeType) shapeComboBox.getSelectedItem();
            editorPanel.setShapeType(selectedShape);
            debugPanel.setShapeType(selectedShape);
            updateAlgorithmComboBox(algorithmComboBox, selectedShape);
        });

        algorithmComboBox.addActionListener(e -> {
            AlgorithmType selectedAlgorithm = (AlgorithmType) algorithmComboBox.getSelectedItem();
            editorPanel.setAlgorithmType(selectedAlgorithm);
            debugPanel.setAlgorithmType(selectedAlgorithm);
        });

        toolBar.add(new JLabel("Фигура: "));
        toolBar.add(shapeComboBox);
        toolBar.add(new JLabel("Алгоритм: "));
        toolBar.add(algorithmComboBox);

        add(toolBar, BorderLayout.NORTH);

        mainTabbedPane.addChangeListener(e -> {
            if (mainTabbedPane.getSelectedComponent() == editor3DPanel) {
                editor3DPanel.requestFocusInWindow();
            }
        });
    }

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
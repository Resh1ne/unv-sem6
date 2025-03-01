package com.example.graphicseditor;

import com.example.algorithms.lb5.PolygonUtils;

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
        PolygonPanel polygonPanel = new PolygonPanel();

        mainTabbedPane.addTab("Редактор 2D", editorPanel);
        mainTabbedPane.addTab("Отладка", debugPanel);
        mainTabbedPane.addTab("Редактор 3D", editor3DPanel);
        mainTabbedPane.addTab("Построение полигонов", polygonPanel);

        add(mainTabbedPane);

        JToolBar toolBar = new JToolBar();
        JComboBox<ShapeType> shapeComboBox = new JComboBox<>(ShapeType.values());
        JComboBox<AlgorithmType> algorithmComboBox = new JComboBox<>(AlgorithmType.values());

        shapeComboBox.addActionListener(e -> {
            ShapeType selectedShape = (ShapeType) shapeComboBox.getSelectedItem();
            editorPanel.setShapeType(selectedShape);
            debugPanel.setShapeType(selectedShape);
            polygonPanel.setShapeType(selectedShape);
            assert selectedShape != null;
            updateAlgorithmComboBox(algorithmComboBox, selectedShape);
        });

        algorithmComboBox.addActionListener(e -> {
            AlgorithmType selectedAlgorithm = (AlgorithmType) algorithmComboBox.getSelectedItem();
            editorPanel.setAlgorithmType(selectedAlgorithm);
            debugPanel.setAlgorithmType(selectedAlgorithm);
            polygonPanel.setAlgorithmType(selectedAlgorithm);
        });

        JButton checkConvexButton = getCheckConvexButton(polygonPanel);

        toolBar.add(new JLabel("Фигура: "));
        toolBar.add(shapeComboBox);
        toolBar.add(new JLabel("Алгоритм: "));
        toolBar.add(algorithmComboBox);
        toolBar.addSeparator();
        toolBar.add(checkConvexButton);

        add(toolBar, BorderLayout.NORTH);

        mainTabbedPane.addChangeListener(e -> {
            if (mainTabbedPane.getSelectedComponent() == editor3DPanel) {
                editor3DPanel.requestFocusInWindow();
            }
        });
    }

    private JButton getCheckConvexButton(PolygonPanel polygonPanel) {
        JButton checkConvexButton = new JButton("Проверить выпуклость");
        checkConvexButton.addActionListener(e -> {
            boolean isConvex = PolygonUtils.isConvex(polygonPanel.getLastCompletedPolygonPoints());
            if (isConvex) {
                JOptionPane.showMessageDialog(this, "Полигон выпуклый.", "Результат проверки", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Полигон невыпуклый.", "Результат проверки", JOptionPane.WARNING_MESSAGE);
            }
        });
        return checkConvexButton;
    }

    private void updateAlgorithmComboBox(JComboBox<AlgorithmType> algorithmComboBox, ShapeType shapeType) {
        algorithmComboBox.removeAllItems();
        switch (shapeType) {
            case LINE:
                algorithmComboBox.addItem(AlgorithmType.DDA);
                algorithmComboBox.addItem(AlgorithmType.BRESENHAM);
                algorithmComboBox.addItem(AlgorithmType.WU);
                break;
            case POLYGON:
                algorithmComboBox.addItem(AlgorithmType.DDA);
                algorithmComboBox.addItem(AlgorithmType.BRESENHAM);
                break;
            case CONVEX_HULL:
                algorithmComboBox.addItem(AlgorithmType.GRAHAM);
                algorithmComboBox.addItem(AlgorithmType.JARVIS);
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
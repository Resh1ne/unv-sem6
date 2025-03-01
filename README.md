# Лабораторная работа №4
## Цель
Разработать графическую программу, выполняющую следующие геометрические преобразования над трехмерным объектом: перемещение, поворот, скалирование, отображение, перспектива. В программе должно быть предусмотрено считывание координат 3D объекта из текстового файла, обработка клавиатуры и выполнение геометрических преобразований в зависимости от нажатых клавиш. Все преобразования следует производить с использованием матричного аппарата и представления координат в однородных координатах.
## Основные теоретические сведения
- Однородные координаты — это система координат, которая позволяет выполнять преобразования, такие как перемещение, поворот и масштабирование, с использованием матриц.
- Матрицы преобразования — это инструменты, используемые для осуществления различных геометрических изменений объектов в пространстве.
## Интерфейс
![image](https://github.com/user-attachments/assets/2b64e18c-4f8e-4af9-90e2-eec2ac02e97c)

## Реализация
### Класс для загрузки объекта
```
public class ObjectLoader {
    public static Object3D loadFromFile(String filename) throws IOException {
        Object3D object = new Object3D();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length < 4) continue;

                    try {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        double z = Double.parseDouble(parts[3]);
                        object.addVertex(x, y, z);
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка чтения координат: " + line);
                    }
                } else if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length < 4) continue;

                    try {
                        int[] vertexIndices = new int[parts.length - 1];
                        for (int i = 1; i < parts.length; i++) {
                            vertexIndices[i - 1] = Integer.parseInt(parts[i].split("/")[0]) - 1;
                        }
                        object.addFace(vertexIndices);
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка чтения индексов граней: " + line);
                    }
                }
            }
        }
        return object;
    }
}
```
### Класс для обработки нажатия клавиш
```
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
            case KeyEvent.VK_W -> object.transform(Matrix4x4.translation(0, 1, 0));
            case KeyEvent.VK_S -> object.transform(Matrix4x4.translation(0, -1, 0));
            case KeyEvent.VK_A -> object.transform(Matrix4x4.translation(-1, 0, 0));
            case KeyEvent.VK_D -> object.transform(Matrix4x4.translation(1, 0, 0));
            case KeyEvent.VK_LEFT -> object.rotateY(-10);
            case KeyEvent.VK_RIGHT -> object.rotateY(10);
            case KeyEvent.VK_UP -> object.rotateX(-10);
            case KeyEvent.VK_DOWN -> object.rotateX(10);
            case KeyEvent.VK_Z -> object.scale(1.2);
            case KeyEvent.VK_X -> object.scale(0.8);
        }
        panel.repaint();
    }
}
```
### Класс представляющий матрицу
```
public class Matrix4x4 {
    private final double[][] matrix = new double[4][4];

    public Matrix4x4() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = (i == j) ? 1 : 0;
            }
        }
    }

    public Matrix4x4 multiply(Matrix4x4 other) {
        Matrix4x4 result = new Matrix4x4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.matrix[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return result;
    }

    public double[] transform(double[] point) {
        double[] result = new double[4];
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += matrix[i][j] * point[j];
            }
        }
        return result;
    }

    public static Matrix4x4 translation(double tx, double ty, double tz) {
        Matrix4x4 m = new Matrix4x4();
        m.matrix[0][3] = tx;
        m.matrix[1][3] = ty;
        m.matrix[2][3] = tz;
        return m;
    }

    public static Matrix4x4 scaling(double sx, double sy, double sz) {
        Matrix4x4 m = new Matrix4x4();
        m.matrix[0][0] = sx;
        m.matrix[1][1] = sy;
        m.matrix[2][2] = sz;
        return m;
    }

    public static Matrix4x4 rotationX(double angle) {
        Matrix4x4 m = new Matrix4x4();
        double rad = Math.toRadians(angle);
        m.matrix[1][1] = Math.cos(rad);
        m.matrix[1][2] = -Math.sin(rad);
        m.matrix[2][1] = Math.sin(rad);
        m.matrix[2][2] = Math.cos(rad);
        return m;
    }

    public static Matrix4x4 rotationY(double angle) {
        Matrix4x4 m = new Matrix4x4();
        double rad = Math.toRadians(angle);
        m.matrix[0][0] = Math.cos(rad);
        m.matrix[0][2] = Math.sin(rad);
        m.matrix[2][0] = -Math.sin(rad);
        m.matrix[2][2] = Math.cos(rad);
        return m;
    }

    public static Matrix4x4 perspective(double fov, double aspect, double near, double far) {
        Matrix4x4 m = new Matrix4x4();
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2);
        m.matrix[0][0] = f / aspect;
        m.matrix[1][1] = f;
        m.matrix[2][2] = (far + near) / (near - far);
        m.matrix[2][3] = (2 * far * near) / (near - far);
        m.matrix[3][2] = -1;
        m.matrix[3][3] = 0;
        return m;
    }
}
```
### Класс представляющий объект
```
public class Object3D {
    private final List<double[]> vertices = new ArrayList<>();
    private final List<int[]> faces = new ArrayList<>();

    public void addVertex(double x, double y, double z) {
        vertices.add(new double[]{x, y, z, 1});
    }

    public void addFace(int[] vertexIndices) {
        faces.add(vertexIndices);
    }

    public List<double[]> getVertices() {
        return vertices;
    }

    public List<int[]> getFaces() {
        return faces;
    }

    public void transform(Matrix4x4 matrix) {
        for (double[] vertex : vertices) {
            double[] transformed = matrix.transform(vertex);
            System.arraycopy(transformed, 0, vertex, 0, 4);
        }
    }

    public void rotateY(double angle) {
        double[] center = findCenter();
        transform(Matrix4x4.translation(-center[0], -center[1], -center[2]));
        transform(Matrix4x4.rotationY(angle));
        transform(Matrix4x4.translation(center[0], center[1], center[2]));
    }

    public void rotateX(double angle) {
        double[] center = findCenter();
        transform(Matrix4x4.translation(-center[0], -center[1], -center[2]));
        transform(Matrix4x4.rotationX(angle));
        transform(Matrix4x4.translation(center[0], center[1], center[2]));
    }

    public void scale(double factor) {
        double[] center = findCenter();
        transform(Matrix4x4.translation(-center[0], -center[1], -center[2]));
        transform(Matrix4x4.scaling(factor, factor, factor));
        transform(Matrix4x4.translation(center[0], center[1], center[2]));
    }

    private double[] findCenter() {
        double x = 0, y = 0, z = 0;
        for (double[] vertex : vertices) {
            x += vertex[0];
            y += vertex[1];
            z += vertex[2];
        }
        int count = vertices.size();
        return new double[]{x / count, y / count, z / count};
    }
}
```
## Технологии
- Java
- JavaFX
- Maven
## Вывод
В процессе выполнения лабораторной работы были изучены основные методы графической визуализации и трансформации трехмерных объектов. Практическая реализация графического редактора на основе матричных преобразований предоставила ценные навыки в области компьютерной графики и геометрии. Основное внимание было уделено использованию однородных координат и их преобразованию, что является ключевым для работы с трехмерной графикой.

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Layer {
    private List<Shape> shapes;

    public Layer() {
        shapes = new ArrayList<>();
    }

    public void addShape(Shape shape) {
        if (shapes == null) {
            shapes = new ArrayList<>();
        }
        shapes.add(shape);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void removeCircles() {
        shapes.removeIf(shape -> shape instanceof Circle);
    }

    public String getInfo() {
        StringBuilder s = new StringBuilder();
        s.append("Layer of crazy shapes:");
        for (Shape shape : shapes) {
            s.append("\n").append(shape.toString());
        }
        return s.toString();
    }

    public void removeDuplicates() {
        Set<Shape> shapeSet = new HashSet<>();
        List<Shape> uniqueShapes = new ArrayList<>();

        for (Shape shape : shapes) {
            if (shapeSet.add(shape)) { // add() returns false if the shape already exists
                uniqueShapes.add(shape);
            }
        }
        this.shapes = uniqueShapes;
    }
}
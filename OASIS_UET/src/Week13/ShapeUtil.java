import java.util.List;
import java.util.stream.Collectors;

public class ShapeUtil {

    public static String printInfo(List<GeometricObject> shapes) {
        StringBuilder info = new StringBuilder();

        /*
            The stream() function in Java is a powerful tool for processing sequences of elements,
            such as collections (e.g., lists, sets).
            Streams allow you to perform various operations on the data,
            such as filtering, mapping, and reducing, in a concise and readable way
         */
        List<Circle> circles = shapes.stream()
                .filter(shape -> shape instanceof Circle) //filter: categorise instance of the object
                .map(shape -> (Circle) shape)
                .collect(Collectors.toList()); //collect: group the objects into list - collect into list

        List<Triangle> triangles = shapes.stream()
                .filter(shape -> shape instanceof Triangle)
                .map(shape -> (Triangle) shape)
                .collect(Collectors.toList());

        info.append("Circle:\n");
        for (Circle circle : circles) {
            info.append(circle.toString()).append("\n");
        }

        info.append("Triangle:\n");
        for (Triangle triangle : triangles) {
            info.append(triangle.toString()).append("\n");
        }

        return info.toString();
    }
}
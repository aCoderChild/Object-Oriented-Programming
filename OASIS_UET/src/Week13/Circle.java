import java.awt.*;

public class Circle implements GeometricObject {
    public static final double PI = 3.14;
    private double radius;
    private Point center;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getArea() {
        return PI * radius * radius;
    }

    public double getPerimeter() {
        return 2 * PI * radius;
    }

    @Override
    public String getInfo() {
        return String.format("Circle[(%.2f,%.2f),r=%.2f]", center.getPointX(), center.getPointY(), radius);
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
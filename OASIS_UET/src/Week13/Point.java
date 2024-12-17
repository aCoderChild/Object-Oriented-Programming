import static java.lang.Math.sqrt;

public class Point {
    private double pointX;
    private double pointY;

    public Point(double pointX, double pointY) {
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public double getPointX() {
        return pointX;
    }

    public void setPointX(double pointX) {
        this.pointX = pointX;
    }

    public double getPointY() {
        return pointY;
    }

    public void setPointY(double pointY) {
        this.pointY = pointY;
    }

    public double distance(Point other) {
        return sqrt((pointX - other.getPointX()) * (pointX - other.getPointX()) + (pointY - other.getPointY()) * (pointY - other.getPointY()));
    }
}
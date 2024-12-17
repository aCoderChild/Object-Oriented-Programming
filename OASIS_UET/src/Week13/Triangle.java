public class Triangle implements GeometricObject {
    private Point p1, p2, p3;

    Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Point getP3() {
        return p3;
    }

    public double getArea() {
        double d1 = getP1().distance(getP2());
        double d2 = getP1().distance(getP3());
        double d3 = getP2().distance(getP3());
        double half = (d1 + d2 + d3) / 2;
        return Math.sqrt(half * (half - d1) * (half - d2) * (half - d3));
    }

    public double getPerimeter() {
        double d1 = getP1().distance(getP2());
        double d2 = getP1().distance(getP3());
        double d3 = getP2().distance(getP3());
        return d1 + d2 + d3;
    }

    @Override
    public String getInfo() {
        return String.format("Triangle[(%.2f,%.2f),(%.2f,%.2f),(%.2f,%.2f)]",
                p1.getPointX(), p1.getPointY(),
                p2.getPointX(), p2.getPointY(),
                p3.getPointX(), p3.getPointY());
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
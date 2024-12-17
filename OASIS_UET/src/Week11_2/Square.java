import java.util.Objects;

public class Square extends Rectangle {
    public Square() {}

    public Square(double side) {
        super(side, side);
    }

    public Square(double side, String Color, boolean filled) {
        super(side, side, Color, filled);
    }

    public Square(Point topLeft, double side, String Color, boolean filled) {
        super(topLeft, side, side, Color, filled);
    }

    public double getSide() {
        return getWidth();
    }

    public void setSide(double side) {
        setWidth(side);
        setLength(side);
    }

    public void setWidth(double side) {
        super.setWidth(side);
        super.setLength(side);
    }

    public void setLength(double side) {
        super.setLength(side);
        super.setWidth(side);
    }

    public boolean equals(Object o) {
        if (o instanceof Square) {
            Square another = (Square) o;
            return Math.abs(this.width - another.getSide())
                    <= 0.001
                    && this.topLeft.equals(another.getTopLeft());
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(topLeft, width);
    }

    public String toString() {
        return "Square[topLeft=("+topLeft.getPointX()+','+topLeft.getPointY()+"),side=" + getSide() + ",color=" + getColor() + ",filled=" + isFilled() + "]";
    }
}
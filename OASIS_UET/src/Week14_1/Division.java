public class Division extends BinaryExpression{
    public Division(Expression left, Expression right) {
        super(left, right);
    }
    public String toString() {
        return left.toString() + " / " + right.toString();
    }

    public double evaluate() {
        if (Double.compare(right.evaluate(), 0) != 0) {
            return left.evaluate() / right.evaluate();
        } else {
            throw new ArithmeticException("Lỗi chia cho 0");
        }
    }
}

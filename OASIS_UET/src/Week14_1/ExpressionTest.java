public class ExpressionTest {
    public static void main(String[] args) {
        Numeral n1 = new Numeral(10);
        Numeral n2 = new Numeral(3);
        Numeral n3 = new Numeral(4);

        Square sq1 = new Square(n1);
        Subtraction su1 = new Subtraction(sq1, n2);
        Multiplication mu1 = new Multiplication(n3, n2);
        Addition ad1 = new Addition(su1, mu1);
        Square sq2 = new Square(ad1);

        System.out.println(sq2);
        System.out.println(sq2.evaluate());
    }
}

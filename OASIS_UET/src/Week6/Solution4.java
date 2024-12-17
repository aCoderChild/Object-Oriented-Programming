/**
 * Fraction class
 */
public class Solution4 {
    private int numerator;
    private int denominator;

    /**
     * Constructor for the Solution class.
     *
     * @param numerator   the numerator of the fraction
     * @param denominator the denominator of the fraction
     */
    public Solution4(int numerator, int denominator) {
        if (denominator != 0) {
            setNumerator(numerator);
            setDenominator(denominator);
        } else {
            setNumerator(numerator);
            setDenominator(1);
        }
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public void setDenominator(int denominator) {
        if (denominator != 0) {
            this.denominator = denominator;
        }
    }

    /**
     * Calculate the greatest common divisor (GCD).
     *
     * @param a the first number
     * @param b the second number
     * @return the GCD of a and b
     */
    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        if (b == 0) return a;
        else return gcd(b, a%b);
    }

    /**
     * Reduce the fraction to its simplest form.
     *
     * @return a new Solution instance in reduced form
     */
    public Solution4 reduce() {
        int gcd = gcd(numerator, denominator);
        if (gcd != 1) {
            return new Solution4(numerator / gcd, denominator / gcd);
        } else {
            return this;
        }
    }

    /**
     * Add another fraction to this fraction.
     *
     * @param fraction the fraction to add
     * @return a new Solution instance representing the result
     */
    public Solution4 add(Solution4 fraction) {
        int newNumerator = (this.numerator * fraction.getDenominator()) + (fraction.getNumerator() * this.denominator);
        int newDenominator = this.denominator * fraction.getDenominator();
        return new Solution4(newNumerator, newDenominator).reduce();
    }

    /**
     * Subtract another fraction from this fraction.
     *
     * @param fraction the fraction to subtract
     * @return a new Solution instance representing the result
     */
    public Solution4 subtract(Solution4 fraction) {
        int newNumerator = (this.numerator * fraction.getDenominator()) - (fraction.getNumerator() * this.denominator);
        int newDenominator = this.denominator * fraction.getDenominator();
        return new Solution4(newNumerator, newDenominator).reduce();
    }

    /**
     * Multiply this fraction by another fraction.
     *
     * @param fraction the fraction to multiply by
     * @return a new Solution instance representing the result
     */
    public Solution4 multiply(Solution4 fraction) {
        int newNumerator = this.numerator * fraction.getNumerator();
        int newDenominator = this.denominator * fraction.getDenominator();
        return new Solution4(newNumerator, newDenominator).reduce();
    }

    /**
     * Divide this fraction by another fraction.
     *
     * @param fraction the fraction to divide by
     * @return a new Solution instance representing the result
     */
    public Solution4 divide(Solution4 fraction) {
        int newNumerator = this.numerator * fraction.getDenominator();
        int newDenominator = this.denominator * fraction.getNumerator();
        return new Solution4(newNumerator, newDenominator).reduce();
    }

    /**
     * Check if this fraction is equal to another object.
     *
     * @param obj the object to compare
     * @return true if the fractions are equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Solution4)) {
            return false;
        }
        Solution4 other = (Solution4) obj;
        other = other.reduce();
        return other.getNumerator() == this.reduce().getNumerator()
                && other.getDenominator() == this.reduce().getDenominator();
    }
}
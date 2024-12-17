// Import your library
// Do not change the name of the Solution class

/**
 * Find the greatest common divisor
 */
public class Solution2 {
    // Type your main code here

    public static int gcd(int a, int b) {
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        if (a == b) {
            return a;
        }
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }
        if (a < b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        int r = a % b;
        while (r != 0) {
            a = b;
            b = r;
            r = a % b;
        }
        return Math.abs(b);
    }
} 
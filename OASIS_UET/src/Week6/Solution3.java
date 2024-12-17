// Import your library
// Do not change the name of the Solution class

/**
 * Check if a number is prime or not
 */
public class Solution3 {
    // Type your main code here
    public boolean isPrime (int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= (n / 2); ++i) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}

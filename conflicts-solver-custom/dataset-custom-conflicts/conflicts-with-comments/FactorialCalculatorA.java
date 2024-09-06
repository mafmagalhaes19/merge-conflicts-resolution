public class FactorialCalculator {
    // The implementation should be recursive
    public int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}


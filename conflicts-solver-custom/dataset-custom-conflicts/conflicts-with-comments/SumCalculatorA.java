public class SumCalculator {
    // We should sum and include n
    public int calculateSum(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
        }
        return sum;
    }
}

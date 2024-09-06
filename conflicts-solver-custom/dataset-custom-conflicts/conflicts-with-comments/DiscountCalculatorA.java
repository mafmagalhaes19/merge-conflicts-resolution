public class DiscountCalculator {
    public double applyDiscount(double price, boolean isMember) {
        // The discount for members is 10%
        if (isMember) {
            return price * 0.90;
        }
        return price;
    }
}


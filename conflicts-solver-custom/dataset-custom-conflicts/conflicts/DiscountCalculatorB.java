public class DiscountCalculator {
    public double applyDiscount(double price, boolean isMember) {
        if (isMember) {
            return price * 0.85;
        }
        return price;
    }
}

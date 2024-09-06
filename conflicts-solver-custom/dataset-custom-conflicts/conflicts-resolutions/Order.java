public class Order {
    private String orderId;
    private double totalAmount;

    public Order(String orderId, double totalAmount) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void applyDiscount(double discountPercentage) {
        totalAmount = totalAmount * (1 - discountPercentage / 100);
    }

    public void cancelOrder() {
        totalAmount = 0;
        System.out.println("Order has been cancelled, refunding amount");
    }
}

public class PaymentProcessor {
    public void processPayment(double amount, String currency) {
        System.out.println("Processing payment of " + amount + " in " + currency);
    }

    public void processPayment(double amount) {
        processPayment(amount, "USD");
    }
}

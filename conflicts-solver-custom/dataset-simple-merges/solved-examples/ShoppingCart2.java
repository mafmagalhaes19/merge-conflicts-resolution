import java.util.HashMap;
import java.util.Map;

public class ShoppingCart2 {
    private Map<String, Double> items;
    private double totalAmount;

    public ShoppingCart2() {
        this.items = new HashMap<>();
        this.totalAmount = 0.0;
    }

    public void addProduct(String product, double price) {
        items.put(product, price);
        totalAmount += price;
    }

    public void removeProduct(String product, double price) {
        items.remove(product);
        totalAmount -= price;
    }

    public Map<String, Double> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public static void main(String[] args) {
        ShoppingCart2 cart = new ShoppingCart2();
        cart.addProduct("Apple", 1.0);
        cart.addProduct("Banana", 0.5);
        System.out.println("Items: " + cart.getItems());
        System.out.println("Total Amount: " + cart.getTotalAmount());
    }
}

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart1 {
    private List<String> items;
    private double totalPrice;

    public ShoppingCart1() {
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public void addItem(String item, double price) {
        items.add(item);
        totalPrice += price;
    }

    public void removeItem(String item, double price) {
        items.remove(item);
        totalPrice -= price;
    }

    public List<String> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public static void main(String[] args) {
        ShoppingCart1 cart = new ShoppingCart1();
        cart.addItem("Apple", 1.0);
        cart.addItem("Banana", 0.5);
        System.out.println("Items: " + cart.getItems());
        System.out.println("Total Price: " + cart.getTotalPrice());
    }
}

import java.util.ArrayList;
import java.util.List;

public class Order1 {
    private int orderId;
    private List<String> items;

    public Order1(int orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public List<String> getItems() {
        return items;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (String item : items) {
            if (item.equals("item1")) {
                total += 12.0; // Different price for item1
            } else if (item.equals("item2")) {
                total += 18.0; // Different price for item2
            }
        }
        return total;
    }

    public static void main(String[] args) {
        Order2 order = new Order2(2); // Different order ID
        order.addItem("item1");
        order.addItem("item3"); // Different item
        System.out.println("Branch B: Order total is " + order.calculateTotal());
    }
}

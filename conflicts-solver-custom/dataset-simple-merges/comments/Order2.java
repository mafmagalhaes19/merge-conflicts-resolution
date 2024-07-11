import java.util.ArrayList;
import java.util.List;

public class Order2 {
    private int orderId;
    private List<String> items;

    public Order2(int orderId) {
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
        // The price for item 1 should be 12.0 and for item 2 should be 18.0
        double total = 0.0;
        for (String item : items) {
            if (item.equals("item1")) {
                total += 12.0; 
            } else if (item.equals("item2")) {
                total += 18.0; 
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

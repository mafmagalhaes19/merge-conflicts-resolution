import java.util.ArrayList;
import java.util.List;

public class Order0 {
    private int orderId;
    private List<String> items;

    public Order0(int orderId) {
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
                total += 10.0;
            } else if (item.equals("item2")) {
                total += 20.0;
            }
        }
        return total;
    }

    public static void main(String[] args) {
        Order0 order = new Order0(1);
        order.addItem("item1");
        order.addItem("item2");
        System.out.println("Order total is " + order.calculateTotal());
    }
}

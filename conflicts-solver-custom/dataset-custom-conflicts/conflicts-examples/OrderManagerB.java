import java.util.Set;
import java.util.HashSet;

public class OrderManager {
    public Set<String> getOrderDetails() {
        Set<String> orders = new HashSet<>();
        orders.add("Order 1");
        return orders;
    }
}
import java.util.Set;
import java.util.HashSet;

public class OrderManager {
    //Should allow repeated orders
    public Set<String> getOrderDetails() {
        Set<String> orders = new HashSet<>();
        orders.add("Order 1");
        return orders;
    }
}

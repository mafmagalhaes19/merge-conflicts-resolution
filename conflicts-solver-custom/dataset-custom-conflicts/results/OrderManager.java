import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class OrderManager {
    public List<String> getOrderDetails() {
        Set<String> orders = new HashSet<>();
        orders.add("Order 1");
        return new ArrayList<>(orders);
    }
}

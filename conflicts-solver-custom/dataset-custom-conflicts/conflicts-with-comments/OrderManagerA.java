import java.util.List;
import java.util.ArrayList;

public class OrderManager {
    //Should allow repeated orders
    public List<String> getOrderDetails() {
        List<String> orders = new ArrayList<>();
        orders.add("Order 1");
        return orders;
    }
}


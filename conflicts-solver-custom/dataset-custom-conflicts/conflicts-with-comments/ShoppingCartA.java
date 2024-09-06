import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    // We can't have repeated elements
    private List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public List<String> getItems() {
        return items;
    }
}

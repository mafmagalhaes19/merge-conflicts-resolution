import java.util.HashSet;
import java.util.Set;

public class ShoppingCart {
    private Set<String> items = new HashSet<>();

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public Set<String> getItems() {
        return items;
    }
}

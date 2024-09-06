public class Inventory {
    private int itemCount;

    // Print an informative message
    public void addItem(int count) {
        itemCount += count;
    }

    public int getItemCount() {
        return itemCount;
    }
}


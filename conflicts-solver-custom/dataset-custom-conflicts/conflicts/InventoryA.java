public class Inventory {
    private int itemCount;

    public void addItem(int count) {
        itemCount += count;
    }

    public int getItemCount() {
        return itemCount;
    }
}

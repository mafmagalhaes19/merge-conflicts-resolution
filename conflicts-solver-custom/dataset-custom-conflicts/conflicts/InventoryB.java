public class Inventory {
    private int itemCount;

    public void addItem(int count) {
        itemCount += count;
        System.out.println("Items added: " + count);
    }

    public int getItemCount() {
        return itemCount;
    }
}


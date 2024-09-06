public class Incrementer {
    private int count = 0;

    public void increment() {
        count++;
    }

    public void incrementByTwo() {
        count += 2;
    }

    public int getCount() {
        return count;
    }
}

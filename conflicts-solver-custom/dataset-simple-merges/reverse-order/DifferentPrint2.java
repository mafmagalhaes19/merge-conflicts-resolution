public class DifferentPrint2 {
    public static void main(String[] args) {
        System.out.println("Hello from Branch A");
        int result = addNumbers(5, 3);
        System.out.println("Result: " + result);
    }

    public static int addNumbers(int a, int b) {
        return a + b;
    }
}

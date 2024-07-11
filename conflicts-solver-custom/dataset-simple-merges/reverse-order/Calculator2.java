public class Calculator2 {
    public static void main(String[] args) {
        System.out.println("Calculator from Branch A");
        int result = multiply(4, 2);
        System.out.println("Multiplication Result: " + result);
    }

    public static int multiply(int a, int b) {
        return a * b;
    }
}


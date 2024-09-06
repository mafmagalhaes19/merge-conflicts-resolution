public class Printer {
    public void displayMessage(String message) {
        if (message != null && !message.isEmpty()) {
            System.out.println(message);
        } else {
            System.out.println("Hello World");
        }
    }
}

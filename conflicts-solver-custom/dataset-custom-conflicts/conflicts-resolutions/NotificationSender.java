public class NotificationSender {
    public void send(String message) {
        System.out.println("Sending message: " + message);
    }

    public void send(String message, String recipient) {
        System.out.println("Sending message to " + recipient + ": " + message);
    }
}

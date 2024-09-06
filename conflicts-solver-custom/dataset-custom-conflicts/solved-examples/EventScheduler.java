public class EventScheduler {
    public void scheduleEvent(String eventName, String eventDate) {
        System.out.println("Event " + eventName + " scheduled on " + eventDate + ".");
    }

    public void scheduleEvent(String eventName) {
        scheduleEvent(eventName, "Unknown date");
    }
}

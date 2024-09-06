public class Countdown {
    public void startCountdown(int seconds) {
        // Time can't be negative
        while (seconds > 0) {
            System.out.println("Countdown: " + seconds);
            seconds--;
        }
    }
}


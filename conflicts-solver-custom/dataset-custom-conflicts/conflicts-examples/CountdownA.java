public class Countdown {
    public void startCountdown(int seconds) {
        while (seconds > 0) {
            System.out.println("Countdown: " + seconds);
            seconds--;
        }
    }
}

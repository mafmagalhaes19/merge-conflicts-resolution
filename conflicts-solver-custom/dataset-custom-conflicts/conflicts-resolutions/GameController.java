public class GameController {
    public void startGame() {
        System.out.println("Game started!");
    }

    public static void startDefaultGame() {
        GameController controller = new GameController();
        controller.startGame();
    }
}


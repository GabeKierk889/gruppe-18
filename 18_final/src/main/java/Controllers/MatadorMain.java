package Controllers;

public class MatadorMain {

    public static void main(String[] args) {

        GameController gameController = GameController.getInstance();

        gameController.initializeGame();

        GameController.getInstance().gameLoop();

    }

}

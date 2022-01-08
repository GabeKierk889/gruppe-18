package Controllers;

public class MatadorMain {

    public static void main(String[] args) {

        GameController gameController = GameController.getInstance();

        gameController.initializeGame();

        ViewController viewController = ViewController.getInstance();

        GameController.getInstance().testMethod();

    }

}

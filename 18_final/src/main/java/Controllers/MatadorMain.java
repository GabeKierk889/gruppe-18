package Controllers;

import Models.Board;
import Models.StreetField;

public class MatadorMain {

    public static void main(String[] args) {

        GameController gameController = GameController.getInstance();

        gameController.initializeGame();

        gameController.gameLoop();

//        gameController.testMethod();

//        Board board = new Board();
//        board.buildHouse();
    }

}

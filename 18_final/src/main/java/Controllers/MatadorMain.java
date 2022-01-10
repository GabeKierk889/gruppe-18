package Controllers;

import Models.Board;

public class MatadorMain {

    public static void main(String[] args) {

        GameController gameController = GameController.getInstance();
//
        gameController.initializeGame();
//
        gameController.gameLoop();

//        gameController.testMethod();

//        Board board = new Board();
//        board.buildHouse();
    }

}

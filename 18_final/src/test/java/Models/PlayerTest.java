package Models;

import Controllers.GameController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = new Player("Xiao");
    Board board = new Board();
    DiceCup diceCup = new DiceCup();

    // this test code is modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021
    @Test
    void movePlayerSteps() {
        GameController.getInstance().initializeGame();
        int playerOnField = player.OnField();
        diceCup.roll();
        int steps = diceCup.getSum();

        player.moveSteps(steps);
        assertEquals(playerOnField + steps,player.OnField());
    }

    @Test
    void collectStartBonus() {
        GameController.getInstance().initializeGame();
        int totalFields = board.getTotalNumOfFields();
        player.moveToField(totalFields-1);

        int balance = player.getAccount().getBalance();
        player.moveSteps(1);
        player.collectStartBonus(1);
        int startBonus = GameSettings.STARTBONUS;
        assertEquals(balance + startBonus,player.getAccount().getBalance());
    }
}
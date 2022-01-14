package Controllers;

import Models.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TC 07
 */
class GameControllerTest {

    // tested by Mark 9 Jan 22
    @Test
    void switchTurn() {
        GameController.getInstance().initializeGame();

        // testing else branch
        boolean extraTurn = true;

        GameController.getInstance().switchTurn(extraTurn);
        assertEquals(1,GameController.getInstance().getCurrentPlayerNum());

        // testing nested if branch
        extraTurn = false;
        GameController.getInstance().switchTurn(extraTurn);
        assertEquals(2,GameController.getInstance().getCurrentPlayerNum());
    }

}
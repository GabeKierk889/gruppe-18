
package Models;

import Controllers.GameController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TC 06
 */
class BoardTest {
    Board board = new Board();

    // tested by Mark 9 Jan 2022
    @Test
    void totalNumOfFields() {
        GameController.getInstance().initializeGame();
        int totalNumOfFields = board.getTotalNumOfFields();
        assertEquals(40,totalNumOfFields);
    }

}
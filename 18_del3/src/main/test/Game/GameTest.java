package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game = new Game("Gabriel", "Mark");

    // tested by Mark 2021-11-23
    @Test
    void switchTurn() {
        // testing else branch
        boolean extraTurn = true;

        game.switchTurn(extraTurn);
        assertEquals(1,game.getCurrentPlayerNumber());

        // testing nested if branch
        extraTurn = false;
        game.switchTurn(extraTurn);
        assertEquals(2,game.getCurrentPlayerNumber());
    }

    @Test
    void checkIsAnyoneBankrupt() {
    }

    // tested by Mark 2021-11-23
    @Test
    void isGameOver() {
        game.getPlayerObject(1).getAccount().setCurrentBalance(0);

        assertEquals(true,game.checkIsAnyoneBankrupt());
    }

    @Test
    void determineWinner() {
    }

    @Test
    void determineWinner2() {
    }

    @Test
    void getBankruptPlayerName() {
    }

    @Test
    void getDie() {
    }

    @Test
    void getBoard() {
    }

    @Test
    void getPlayerObject() {
    }

    @Test
    void getCurrentPlayerNumber() {
    }

    @Test
    void getTotalPlayers() {
    }

    @Test
    void setCurrentPlayer() {
    }
}
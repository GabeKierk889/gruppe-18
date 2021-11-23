package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game1 = new Game("Gabriel", "Mark");

    // tested by Mark 2021-11-23
    @Test
    void switchTurn() {
        // testing else branch
        boolean extraTurn = true;

        game1.switchTurn(extraTurn);
        assertEquals(1,game1.getCurrentPlayerNumber());

        // testing nested if branch
        extraTurn = false;
        game1.switchTurn(extraTurn);
        assertEquals(2,game1.getCurrentPlayerNumber());
    }

    @Test
    void checkIsAnyoneBankrupt() {
    }

    @Test
    void isGameOver() {
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
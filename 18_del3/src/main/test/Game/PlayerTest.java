package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = new Player("Xiao");
    Board board = new Board();
    Die die = new Die();

    // test af alle
    @Test
    void movePlayerSteps() {
        int playerOnField = player.OnField();
        die.roll();
        int steps = die.getFaceValue();

        player.movePlayerSteps(steps);
        assertEquals(playerOnField + steps,player.OnField());
    }

    @Test
    void movePlayerStepsBoundary(){
        int totalFields = Board.getTotalNumberOfFields();
        player.movePlayerToField(totalFields-1);

        player.movePlayerSteps(1);
        assertEquals(0,player.OnField());
    }

    @Test
    void collectStartBonus() {
        int totalFields = Board.getTotalNumberOfFields();
        player.movePlayerToField(totalFields-1);

        int balance = player.getAccount().getBalance();
        player.movePlayerSteps(1);
        player.collectStartBonus(1);
        int startBonus = Account.STARTBONUS;
        assertEquals(balance + startBonus,player.getAccount().getBalance());
    }

    @Test
    void collectStartBonusNotEligibleForBonus() {

        int balance = player.getAccount().getBalance();
        player.movePlayerSteps(1);
        player.collectStartBonus(1);
        assertEquals(balance,player.getAccount().getBalance());
    }

    @Test
    void movePlayerToField() {
    }

    @Test
    void testMovePlayerToField() {
    }

    @Test
    void getName() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void onField() {
    }

    @Test
    void setIsBankrupt() {
    }

    @Test
    void setIsInJail() {
    }

    @Test
    void getIsInJail() {
    }

    @Test
    void getIsBankrupt() {
    }

    @Test
    void giveReleaseFromJailCard() {
    }

    @Test
    void returnReleaseFromJailCard() {
    }

    @Test
    void hasAReleaseFromJailCard() {
    }
}
package Models.ChanceCardSubType;

import Controllers.GameController;
import Controllers.ViewController;
import Models.Board;
import Models.ChanceCard;
import Models.Player;

public class PayMoneyForBuildingsCard extends ChanceCard {
    private final int PAYPERHOUSE, PAYPERHOTEL;

    public PayMoneyForBuildingsCard(String text, int[] amount) {
        super(text);
        PAYPERHOUSE = amount[0];
        PAYPERHOTEL = amount[1]; }

    @Override
    public void effect(Player currentplayerobject) {
        Board board = GameController.getInstance().getBoard();
        int[] buildingsOwned = board.numBuildingsOwnedByCurrentPlayer();
        int payment = buildingsOwned[1] * PAYPERHOTEL + buildingsOwned[0] * PAYPERHOUSE;
        if (payment > 0) {
        currentplayerobject.getAccount().withdrawMoney(payment);
        // TODO: gui
        // write a message to player via gui on how much needs to be paid for your x houses and y hotels
        // e.g. "you need to pay x for your x houses and x hotels", "pay"
            ViewController.getInstance().updateGUIBalance();
        }
    }
}

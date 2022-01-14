package Models.ChanceCardSubType;

import Controllers.GameController;
import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;
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
        int[] buildingsOwned = board.numBuildingsOwnedByPlayer(GameController.getInstance().getCurrentPlayerNum());
        int payment = buildingsOwned[1] * PAYPERHOTEL + buildingsOwned[0] * PAYPERHOUSE;
        if (payment > 0) {
            // displays a message that the player must pay money for their owned buildings
            ViewController_GUIMessages.getInstance().showTakeTurnMessage(54,""+payment,"","");
            currentplayerobject.getAccount().withdrawMoney(payment);
            ViewController.getInstance().updateGUIBalance();
        }
    }
}

package Models.ChanceCardSubType;

import Controllers.GameController;
import Controllers.ViewController;
import Models.ChanceCard;
import Models.Player;

public class ReceiveMatadorStipendCard extends ChanceCard {
    private final int AMOUNT, MAXASSETVALUE;

    public ReceiveMatadorStipendCard(String text, int[] amount) {
        super(text);
        AMOUNT = amount[0];
        MAXASSETVALUE = amount[1]; }

    @Override
    public void effect(Player currentplayerobject) {
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        int assetsOwned = GameController.getInstance().calculateAssets(currentPlayerNum);
        if (assetsOwned <= MAXASSETVALUE) {
            currentplayerobject.getAccount().depositMoney(AMOUNT);
            // TODO: gui
            // write a message to player via gui that they qualify for and will receive the stipend
            // as their assets are worth x (below the limit)
            ViewController.getInstance().updateGUIBalance();
        }
        else
            ; // write a message to player that they will not receive the stipend
                // as their assets are worth x (more than the limit)
    }

}

package Models.ChanceCardSubType;

import Controllers.GameController;
import Models.ChanceCard;
import Models.Player;

public class ReceiveFromEachPlayerCard extends ChanceCard {
    private final int receiveAmount;

    public ReceiveFromEachPlayerCard(String text, int receiveAmount) {
        super(text);
        this.receiveAmount = receiveAmount;
    }

    @Override
    public void effect(Player currentplayerobject) {
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        for (int i = 0; i< GameController.getInstance().getTotalPlayers(); i++)
        {
            if(currentPlayerNum != i + 1 && !GameController.getInstance().getPlayerObject(i + 1).getIsBankrupt())
            {
                GameController.getInstance().getPlayerObject(i + 1).getAccount().transferMoney(receiveAmount, currentPlayerNum);
            }
        }
        // update gui bank balance
    }
}

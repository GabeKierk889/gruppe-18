package Models.ChanceCardSubType;

import Controllers.GameController;
import Models.ChanceCard;
import Models.Player;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class ReceiveFromEachPlayerCard extends ChanceCard {
    private final int AMOUNT;

    public ReceiveFromEachPlayerCard(String text, int[] amount) {
        super(text);
        AMOUNT = amount[0];
    }

    @Override
    public void effect(Player currentplayerobject) {
        int currentPlayerNum = GameController.getInstance().getCurrentPlayerNum();
        for (int i = 0; i< GameController.getInstance().getTotalPlayers(); i++)
        {
            if(currentPlayerNum != i + 1 && !GameController.getInstance().getPlayerObject(i + 1).getIsBankrupt())
            {
                GameController.getInstance().getPlayerObject(i + 1).getAccount().transferMoney(AMOUNT, currentPlayerNum);
            }
        }
        // update gui bank balance
    }
}

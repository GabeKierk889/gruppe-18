package Models.ChanceCardSubType;

import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;
import Models.ChanceCard;
import Models.Player;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

public class PayMoneyCard extends ChanceCard {
    private final int AMOUNT;

    public PayMoneyCard(String text, int[] amount) {
        super(text);
        AMOUNT = amount[0];
    }

    @Override
    public void effect(Player currentplayerobject) {
        currentplayerobject.getAccount().withdrawMoney(AMOUNT);
        ViewController.getInstance().updateGUIBalance();
    }
}


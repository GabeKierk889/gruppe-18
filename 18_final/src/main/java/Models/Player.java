package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

import Controllers.GameController;
import Controllers.ViewController;
import Controllers.ViewController_GUIMessages;

public class Player {
    private final String NAME;
    private final Account ACCOUNT;
    private int onField;
    private boolean isBankrupt; // Keeps track of whether a player has gone bankrupt
    private boolean isInJail;
    private ChanceCard releaseFromJailCard, releaseFromJailCard2; // a player can own max 2 jail chance cards in Matador
    private int throwTwoOfSameDiceInARow;

    public Player(String NAME){
        ACCOUNT = new Account(NAME);
        this.NAME = NAME;
        onField = 0;
        isBankrupt = false;
        isInJail = false;
        throwTwoOfSameDiceInARow = 0;
    }

    // moves the player's location stepsToMove steps forward
    public int moveSteps(int stepsToMove) {
        onField = (onField+stepsToMove) % GameController.getInstance().getBoard().getTotalNumOfFields();
        return onField; }

    // checks if a player is eligible for a start bonus, and deposits the money if they are eligible
    public void collectStartBonus(int diceThrow) {
        // only applies to regular turns/ passing START by throwing the dice, not chance card situations
        if (onField < diceThrow) {
            getAccount().depositMoney(GameSettings.STARTBONUS);
            ViewController.getInstance().updateGUIBalance();
            ViewController_GUIMessages.getInstance().startBonusMessage();
        }
    }

    // moves player to a specified field num
    public void moveToField(int fieldArrayNum) {
        if(fieldArrayNum >= 0)
            onField = fieldArrayNum % GameController.getInstance().getBoard().getTotalNumOfFields();
    }

    public void giveReleaseFromJailCard (ChanceCard jailCard) {
        // if player is given a release from jail card, they get to keep it (max 2 cards)
        if (releaseFromJailCard == null)
            releaseFromJailCard = jailCard;
        else
            releaseFromJailCard2 = jailCard; }

    public ChanceCard returnReleaseFromJailCard () {
        // if a jail card is taken away from a player, the 2nd one is taken first (if they have 2)
        ChanceCard temp;
        if (releaseFromJailCard2 != null) {
            temp = releaseFromJailCard2;
            releaseFromJailCard2 = null;
        }
        else {
            temp = releaseFromJailCard;
            releaseFromJailCard = null;
        }
        return temp;
    }

    public String getName() {
        return NAME;
    }
    public Account getAccount() {
        return ACCOUNT;
    }
    public int OnField() {return onField;}
    public void setIsBankrupt(boolean isPlayerBankrupt) { this.isBankrupt = isPlayerBankrupt; }
    public void setIsInJail (boolean isInJail) { this.isInJail = isInJail; }
    public boolean getIsInJail () { return isInJail; }
    public boolean getIsBankrupt () { return isBankrupt; }
    public boolean hasAReleaseFromJailCard () {
        return releaseFromJailCard != null;
    }
    public void increaseThrowTwoOfSameCounter() {
        throwTwoOfSameDiceInARow++;
    }
    public void resetThrowTwoOfSameCounter() {
        throwTwoOfSameDiceInARow = 0;
    }
    public int getThrowTwoOfSameDiceInARow() {
        return throwTwoOfSameDiceInARow;
    }
}


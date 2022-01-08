package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

import Controllers.GameController;

public class Player {
    private final String name;
    private final Account account;
    private int onField;
    private boolean isBankrupt; // Keeps track of whether a player has gone bankrupt
    private boolean isInJail;
    private ChanceCard releaseFromJailCard, releaseFromJailCard2; // a player can own max 2 jail chance cards in Matador

    public Player(String name){
        account = new Account();
        this.name = name;
        onField = 0;
        isBankrupt = false;
        isInJail = false;
    }

    // TODO: Bug?
    public int movePlayerSteps(int stepsToMove) {
        onField = (onField+stepsToMove) % GameController.getBoard().getTotalNumOfFields();
        return onField; }

    public void collectStartBonus(int diceThrow) {
        // only applies to regular turns/ passing START by throwing the dice, not chance card situations
        if (onField < diceThrow)
            getAccount().depositMoney(GameSettings.STARTBONUS);
    }

    public void movePlayerToField(int fieldArrayNumber) {
        if(fieldArrayNumber >= 0)
            onField = fieldArrayNumber % GameController.getBoard().getTotalNumOfFields();
    }

    public String getName() {
        return name;
    }
    public Account getAccount() {
        return account;
    }
    public int OnField() {return onField;}
    public void setIsBankrupt(boolean isPlayerBankrupt) { this.isBankrupt = isPlayerBankrupt; }
    public void setIsInJail (boolean isInJail) { this.isInJail = isInJail; }
    public boolean getIsInJail () { return isInJail; }
    public boolean getIsBankrupt () { return isBankrupt; }

    public void giveReleaseFromJailCard (ChanceCard card) {
        // if player is given a release from jail card, they get to keep it (max 2 cards)
        if (releaseFromJailCard == null)
            releaseFromJailCard = card;
        else
            releaseFromJailCard2 = card; }

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

    public boolean hasAReleaseFromJailCard () {
        return releaseFromJailCard != null;
    }
}


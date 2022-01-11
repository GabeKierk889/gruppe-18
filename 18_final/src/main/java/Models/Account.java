package Models;

//This code has been modified from previous assignment CDIO 3 by Maj Kyllesbech, Gabriel H, Kierkegaard, Mark Bidstrup & Xiao Chen handed in 26. November 2021

import Controllers.GameController;

public class Account {

    private int balance;
    private final String NAME;

    public Account (String ownerName) {
        NAME = ownerName;
        balance = GameSettings.STARTINGBALANCE;
    }

    // Withdraws money.
    public void withdrawMoney (int withdrawal) {
        // currently allowing a negative balance
        if (withdrawal > 0) {
            this.balance -= withdrawal; }
        if (balance < 0) {
            GameController.getInstance().sellAssets(NAME,withdrawal,0);
        }
    }

    // method overloading - this method is called in transferMoney(), keeps track of creditorPlayerNum
    public void withdrawMoney (int withdrawal, int creditorPlayerNum) {
        // currently allowing a negative balance
        if (withdrawal > 0) {
            this.balance -= withdrawal; }
        if (balance < 0) {
            GameController.getInstance().sellAssets(NAME,withdrawal,creditorPlayerNum);
        }
    }

    // Deposits money.
    public void depositMoney(int deposit) {
        // Only allows depositing a positive amount to the current balance.
        if (deposit > 0) {
            this.balance += deposit;
        }
    }

    public void transferMoney(int amount, int recipientPlayerNum) {
        withdrawMoney(amount, recipientPlayerNum); // this includes calls to sell assets / go bankrupt methods
        if (balance >= 0)
            GameController.getInstance().getPlayerObject(recipientPlayerNum).getAccount().depositMoney(amount);
    }

    public void setBalance(int balance) {
        if (balance >= 0) {
            this.balance = balance;
        }
    }

    public int getBalance() {
        return balance;
    }

}

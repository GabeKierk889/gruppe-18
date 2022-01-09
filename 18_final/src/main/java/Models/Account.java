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
        if (balance<0) {
            GameController.getInstance().sellAssets(NAME,withdrawal);
        }

        // Only allows withdrawal if the amount is greater than zero and also the withdrawal amount is greater than
        // the current balance.
//        if (withdrawal > 0) {
//            if (withdrawal <= currentBalance) {
//                this.currentBalance -= withdrawal;
//            } else {
//                // If the withdrawal amount is greater than the account balance, then the account balance i reset
//                // to zero.
//                this.currentBalance = 0;
//            }
//        }
    }

    // Deposits money.
    public void depositMoney(int deposit) {
        // Only allows depositing a positive amount to the current balance.
        if (deposit > 0) {
            this.balance += deposit;
        }
    }

    public void transferMoney(int amount, int recipientPlayerNum) {
        withdrawMoney(amount); // this includes calls to sell assets / go bankrupt methods
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
